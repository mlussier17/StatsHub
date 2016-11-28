package ml.statshub.statshub.Marqueur.Hockey;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ml.statshub.statshub.Class.NumbersAvailable;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;

import static ml.statshub.statshub.R.id.awayResult;
import static ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_NUMBERS_PLAYERS;

public class HStartGamesActivity extends AppCompatActivity {
    static public Bundle bundle;
    static public Context context;
    private TextView away;
    private TextView home;
    private TextView awayResultText;
    private TextView homeResultText;
    public RadioGroup teams;
    static public EditText goal;
    static public EditText penalty;
    static public EditText editAssist1;
    static public EditText editAssist2;
    static public EditText editMinutes;
    public RadioButton awayGoal;
    public RadioButton homeGoal;
    public RadioButton ppgGoal;
    public RadioButton pkgGoal;
    public Button sender;
    public Results results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        results = new Results();
        results.setAway(0);
        results.setHome(0);
        setContentView(R.layout.activity_hstart_games);
        context = this.getApplicationContext();
        bundle = getIntent().getExtras();
        away = (TextView)findViewById(R.id.away);
        home = (TextView)findViewById(R.id.home);
        awayResultText = (TextView)findViewById(awayResult);
        homeResultText = (TextView)findViewById(R.id.homeResult);
        away.setText(bundle.getString("awayName"));
        home.setText(bundle.getString("homeName"));
        awayResultText.setText(results.getAway() + "");
        homeResultText.setText(results.getHome() + "");

        new BackgroundGetHockeyNumbersAway().execute();
        new BackgroundGetHockeyNumbersHome().execute();
    }

    public void addGoal(View v){
        final Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.hockeypopupgoal);
        teams = (RadioGroup) builder.findViewById(R.id.radioGroup);
        goal = (EditText)builder.findViewById(R.id.editGoal);
        goal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(goal.getText().length() == 0)
                        Toast.makeText(HStartGamesActivity.context,"Le but doit avoir un numéro", Toast.LENGTH_SHORT).show();
                }
            }
        });
        awayGoal = (RadioButton)builder.findViewById(R.id.awayGoal);
        homeGoal = (RadioButton)builder.findViewById(R.id.homeGoal);
        editAssist1 = (EditText)builder.findViewById(R.id.editAssist1);
        editAssist2 = (EditText)builder.findViewById(R.id.editAssist2);
        ppgGoal = (RadioButton)builder.findViewById(R.id.PPG);
        pkgGoal = (RadioButton)builder.findViewById(R.id.PKG);
        sender = (Button)builder.findViewById(R.id.send);

        sender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String assist1 = editAssist1.getText().toString();
                String assist2 = editAssist2.getText().toString();
                if(goal.getText().length() > 0) {
                    //Add goal
                    if (validate(1)) {
                        if (awayGoal.isChecked()) {
                            if (ppgGoal.isChecked())
                                new BackgroundTaskHockeyPostGoal(HStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(goal.getText().toString()), 1, 0).execute();
                            else if (pkgGoal.isChecked())
                                new BackgroundTaskHockeyPostGoal(HStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(goal.getText().toString()), 0, 1).execute();
                            else
                                new BackgroundTaskHockeyPostGoal(HStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(goal.getText().toString()), 0, 0).execute();
                            results.setAway(1);
                            awayResultText.setText(results.getAway() + "");
                        } else {
                            if (ppgGoal.isChecked())
                                new BackgroundTaskHockeyPostGoal(HStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(goal.getText().toString()), 1, 0).execute();
                            else if (pkgGoal.isChecked())
                                new BackgroundTaskHockeyPostGoal(HStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(goal.getText().toString()), 0, 1).execute();
                            else
                                new BackgroundTaskHockeyPostGoal(HStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(goal.getText().toString()), 0, 0).execute();
                            results.setHome(1);
                            homeResultText.setText(results.getHome() + "");
                        }
                    }
                    //Add assist
                    if (!assist1.isEmpty()) {
                        if (validate(3)) {
                            if (awayGoal.isChecked())
                                new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(editAssist1.getText().toString())).execute();
                            else
                                new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(editAssist1.getText().toString())).execute();
                        }
                    }
                    if (!assist2.isEmpty()) {
                        if (validate(3)) {
                            if (awayGoal.isChecked())
                                new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(editAssist2.getText().toString())).execute();
                            else
                                new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(editAssist2.getText().toString())).execute();
                        }
                    }
                }
                else Toast.makeText(HStartGamesActivity.context,"Le but doit avoir un numéro", Toast.LENGTH_SHORT).show();

                builder.dismiss();
            }
        });
        builder.show();
    }

    public Boolean validate(int id){
        int but;
    Boolean validate = true;
        if(id == 1)
            but = Integer.parseInt(goal.getText().toString());
        else if(id == 2)
            but = Integer.parseInt(penalty.getText().toString());
        else
            but = Integer.parseInt(editAssist1.getText().toString());

        NumbersAvailable numero;
        numero = homeGoal.isChecked()? BackgroundGetHockeyNumbersHome.numbers: BackgroundGetHockeyNumbersAway.numbers;
        for (int i = 0; i < numero.getNumbers().size();i++){
            if( !(but == numero.getNumbers().get(i))) validate = false;
            else return true;
        }
        if(!validate)Toast.makeText(this, "Le numéro n'est pas enregistré avec cette équipe", Toast.LENGTH_SHORT).show();
        return validate;
    }

    public void addPenalty(View v){
        final Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.hockeypopuppenalty);

        teams = (RadioGroup) builder.findViewById(R.id.radioGroup);
        awayGoal = (RadioButton)builder.findViewById(R.id.awayPenalty);
        homeGoal = (RadioButton)builder.findViewById(R.id.homePenalty);
        penalty = (EditText)builder.findViewById(R.id.editPenalty);
        editMinutes = (EditText)builder.findViewById(R.id.editMinutes);
        sender = (Button)builder.findViewById(R.id.send);


        sender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(editMinutes.getText().length() > 0) {
                    if (validate(2)) {
                        if (awayGoal.isChecked())
                            new BackgroundTaskHockeyPostPenalty(bundle.getInt("awayID"), Integer.parseInt(penalty.getText().toString()), Integer.parseInt(editMinutes.getText().toString())).execute();
                        else
                            new BackgroundTaskHockeyPostPenalty(bundle.getInt("homeID"), Integer.parseInt(penalty.getText().toString()), Integer.parseInt(editMinutes.getText().toString())).execute();
                    }
                }
                else Toast.makeText(HStartGamesActivity.context,"La pénalité doit avoir un numéro", Toast.LENGTH_SHORT).show();
                builder.dismiss();
            }
        });
        builder.show();
    }

    public void endOfGame(View v){
        NumbersAvailable numeroAway;
        NumbersAvailable numeroHome;
        numeroAway = BackgroundGetHockeyNumbersAway.numbers;
        numeroHome = BackgroundGetHockeyNumbersHome.numbers;
        new BackgroundTaskHockeyEndGamePlayers(HStartGamesActivity.bundle.getInt("awayID"), numeroAway).execute();
        new BackgroundTaskHockeyEndGamePlayers(HStartGamesActivity.bundle.getInt("homeID"), numeroHome).execute();

    }
}

class BackgroundTaskHockeyEndGameTeams extends AsyncTask<Void,Void,String>{

    @Override
    protected String doInBackground(Void... params) {
        return null;
    }
}

class BackgroundTaskHockeyEndGamePlayers extends AsyncTask<Void,Void,String>{

    private String URLQuery;
    private int teamId;
    private String jsonString;
    private HashMap<String,String> hMap = new HashMap<>();
    NumbersAvailable numero;

    BackgroundTaskHockeyEndGamePlayers(int teamId, NumbersAvailable numero){
        this.teamId = teamId;
        this.numero = numero;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_END_GAME;}

    @Override
    protected String doInBackground(Void... params) {
        for(int i = 0; i < numero.getNumbers().size(); i++) {
            hMap.put("id", teamId + "");
            hMap.put("number", numero.getNumbers().get(i) + "");
            HTTPRequest request = new HTTPRequest();
            jsonString = request.postQueryToHDB(URLQuery, hMap);
        }
        return jsonString;
    }
}

class BackgroundTaskHockeyPostGoal extends AsyncTask<Void,Void,String> {
    private String URLQuery;
    private HashMap<String,String> hMap = new HashMap<>();
    private int teamid;
    private int number;
    private int ppg;
    private int pkg;
    public String jsonString;

    BackgroundTaskHockeyPostGoal(int id, int number, int ppg, int pkg){
        teamid = id;
        this.number = number;
        this.ppg = ppg;
        this.pkg = pkg;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_ADD_GOAL;}

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",teamid + "");
        hMap.put("number",number + "");
        hMap.put("ppg", ppg + "");
        hMap.put("pkg", pkg + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URLQuery,hMap);

        return jsonString;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(HStartGamesActivity.context,"But inseré",Toast.LENGTH_SHORT).show();
    }
}

class BackgroundTaskHockeyPostAssist extends AsyncTask<Void,Void,String> {
    private String URLQuery;
    private HashMap<String,String> hMap = new HashMap<>();
    private int teamid;
    private int number;
    public String jsonString;

    BackgroundTaskHockeyPostAssist(int id, int number){
        teamid = id;
        this.number = number;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_ADD_ASSIST;}

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",teamid + "");
        hMap.put("number",number + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URLQuery,hMap);

        return jsonString;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(HStartGamesActivity.context,"Aide inseré",Toast.LENGTH_SHORT).show();
    }
}

class BackgroundTaskHockeyPostPenalty extends AsyncTask<Void,Void,String> {
    private String URLQuery;
    private HashMap<String,String> hMap = new HashMap<>();
    private int teamid;
    private int number;
    public String jsonString;
    public int minutes;

    BackgroundTaskHockeyPostPenalty(int id, int number, int minutes){
        teamid = id;
        this.number = number;
        this.minutes = minutes;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_ADD_PENALTY;}

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",teamid + "");
        hMap.put("number",number + "");
        hMap.put("penalty",minutes + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URLQuery,hMap);

        return jsonString;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(HStartGamesActivity.context,"Penalité inseré",Toast.LENGTH_SHORT).show();
    }
}

class BackgroundGetHockeyNumbersAway extends AsyncTask<Void,Void,String>{
    private String jsonString;
    private HashMap<String,String> hMap = new HashMap<>();
    static public NumbersAvailable numbers;

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id", HStartGamesActivity.bundle.getInt("awayID") + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URL_HOCKEY_NUMBERS_PLAYERS,hMap);
        return jsonString;
    }

    @Override
    protected void onPostExecute(String s){
        List<Integer> number = new ArrayList<>();
        numbers = new NumbersAvailable();
        try{
            JSONArray jArray = new JSONArray(s);
            for (int i = 0; i < jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                number.add(json_data.getInt("Number"));
            }
            numbers.setIdGames(HStartGamesActivity.bundle.getInt("id"));
            numbers.setIdTeams(HStartGamesActivity.bundle.getInt("awayID"));
            numbers.setNumbers(number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

class BackgroundGetHockeyNumbersHome extends AsyncTask<Void,Void,String>{
    private String jsonString;
    private HashMap<String,String> hMap = new HashMap<>();
    static public  NumbersAvailable numbers;

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id", HStartGamesActivity.bundle.getInt("homeID") + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URL_HOCKEY_NUMBERS_PLAYERS,hMap);
        return jsonString;
    }

    @Override
    protected void onPostExecute(String s){
        List<Integer> number = new ArrayList<>();
        numbers = new NumbersAvailable();
        try{
            JSONArray jArray = new JSONArray(s);
            for (int i = 0; i < jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                number.add(json_data.getInt("Number"));
            }
            numbers.setIdGames(HStartGamesActivity.bundle.getInt("id"));
            numbers.setIdTeams(HStartGamesActivity.bundle.getInt("homeID"));
            numbers.setNumbers(number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
