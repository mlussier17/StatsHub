package ml.statshub.statshub.Marqueur.Hockey;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
    public TextView awayScore;
    public TextView homeScore;
    public RadioButton otCheck;
    public RadioButton soCheck;
    public Boolean validateAssist = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hstart_games);
        results = new Results();
        away = (TextView)findViewById(R.id.away);
        home = (TextView)findViewById(R.id.home);
        awayResultText = (TextView)findViewById(awayResult);
        homeResultText = (TextView)findViewById(R.id.homeResult);
        if (savedInstanceState == null){
            bundle = getIntent().getExtras();
            results.setId(bundle.getInt("id"));
            results.setAway(0);
            results.setHome(0);
            away.setText(bundle.getString("awayName"));
            home.setText(bundle.getString("homeName"));
        }
        else{
            super.onRestoreInstanceState(savedInstanceState);
            results.setId(savedInstanceState.getInt("id"));
            results.setAway(savedInstanceState.getInt("awayScore"));
            results.setHome(savedInstanceState.getInt("homeScore"));
            away.setText(bundle.getString("awayName"));
            home.setText(bundle.getString("homeName"));
        }

        context = this.getApplicationContext();
        awayResultText.setText(results.getAway() + "");
        homeResultText.setText(results.getHome() + "");

        new BackgroundGetHockeyNumbersAway().execute();
        new BackgroundGetHockeyNumbersHome().execute();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("id", bundle.getInt("id"));
        savedInstanceState.putInt("awayScore", results.getAway());
        savedInstanceState.putInt("homeScore", results.getHome());
        savedInstanceState.putString("awayName", bundle.getString("awayName"));
        savedInstanceState.putString("homeName", bundle.getString("homeName"));

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this,"Impossible de fermer la fenêtre si la partie n'est pas terminé",Toast.LENGTH_LONG).show();
    }

    public void addGoal(View v){
        final Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.hockeypopupgoal);
        teams = (RadioGroup) builder.findViewById(R.id.radioGroup);
        goal = (EditText)builder.findViewById(R.id.editGoal);
        editAssist1 = (EditText)builder.findViewById(R.id.editAssist1);
        editAssist2 = (EditText)builder.findViewById(R.id.editAssist2);
        goal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(goal.getText().length() == 0)
                        Toast.makeText(HStartGamesActivity.context,"Le but doit avoir un numéro", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editAssist1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(editAssist1.getText().length() > 0)
                        if(goal.getText().length() > 0) {
                            if (Integer.parseInt(editAssist1.getText().toString()) == Integer.parseInt(goal.getText().toString()))
                                validateAssist = false;
                        }
                }
            }
        });
        editAssist2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(editAssist2.getText().length() > 0) {
                        if (Integer.parseInt(editAssist2.getText().toString()) == Integer.parseInt(goal.getText().toString()))
                            validateAssist = false;
                        if(editAssist1.getText().length() > 0) {
                            if (Integer.parseInt(editAssist2.getText().toString()) == Integer.parseInt(editAssist1.getText().toString()))
                                validateAssist = false;
                        }
                    }

                }
            }
        });
        awayGoal = (RadioButton)builder.findViewById(R.id.awayGoal);
        homeGoal = (RadioButton)builder.findViewById(R.id.homeGoal);
        ppgGoal = (RadioButton)builder.findViewById(R.id.PPG);
        pkgGoal = (RadioButton)builder.findViewById(R.id.PKG);
        sender = (Button)builder.findViewById(R.id.send);

        sender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String assist1 = editAssist1.getText().toString();
                String assist2 = editAssist2.getText().toString();
                if(editAssist2.getText().length() > 0) {
                    if (goal.getText().length() > 0){
                        if (Integer.parseInt(editAssist2.getText().toString()) == Integer.parseInt(goal.getText().toString()))
                            validateAssist = false;
                    }
                    if (editAssist1.getText().length() > 0) {
                        if (Integer.parseInt(editAssist2.getText().toString()) == Integer.parseInt(editAssist1.getText().toString()))
                            validateAssist = false;
                    }
                }
                if(editAssist1.getText().length() > 0) {
                    if(goal.getText().length() > 0) {
                        if (Integer.parseInt(goal.getText().toString()) == Integer.parseInt(editAssist1.getText().toString()))
                            validateAssist = false;
                    }
                }
                if(goal.getText().length() > 0 && validateAssist) {
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
                            if (validate(4)) {
                                if (awayGoal.isChecked())
                                    new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(editAssist2.getText().toString())).execute();
                                else
                                    new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(editAssist2.getText().toString())).execute();
                            }
                        }
                    }
                }
                else Toast.makeText(HStartGamesActivity.context,"Le but est soit vide ou une il y a une répétition de chiffre", Toast.LENGTH_SHORT).show();
                validateAssist = true;
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
        else if (id == 3)
            but = Integer.parseInt(editAssist1.getText().toString());
        else
            but = Integer.parseInt(editAssist2.getText().toString());

        NumbersAvailable numero;
        numero = homeGoal.isChecked()? BackgroundGetHockeyNumbersHome.numbers: BackgroundGetHockeyNumbersAway.numbers;
        if(numero.getNumbers().size() == 0) validate = false;
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
        final NumbersAvailable numeroAway;
        final NumbersAvailable numeroHome;
        numeroAway = BackgroundGetHockeyNumbersAway.numbers;
        numeroHome = BackgroundGetHockeyNumbersHome.numbers;

        final Dialog builder1 = new Dialog(this);
        builder1.setContentView(R.layout.hockeypopupend);
        awayScore = (TextView)builder1.findViewById(R.id.editAwayResult);
        homeScore= (TextView)builder1.findViewById(R.id.editHomeResult);
        awayGoal = (RadioButton)builder1.findViewById(R.id.awayGoal);
        homeGoal = (RadioButton)builder1.findViewById(R.id.homeGoal);
        otCheck = (RadioButton)builder1.findViewById(R.id.OT);
        soCheck= (RadioButton)builder1.findViewById(R.id.SO);
        sender = (Button)builder1.findViewById(R.id.send);
        awayScore.setText(results.getAway() + "");
        homeScore.setText(results.getHome() + "");
        awayGoal.setEnabled(false);
        homeGoal.setEnabled(false);
        if(results.getAway() > results.getHome())awayGoal.setChecked(true);
        else homeGoal.setChecked(true);

        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validate = false;
                new BackgroundTaskHockeyEndGamePlayers(HStartGamesActivity.bundle.getInt("awayID"), numeroAway).execute();
                new BackgroundTaskHockeyEndGamePlayers(HStartGamesActivity.bundle.getInt("homeID"), numeroHome).execute();
                if(awayGoal.isChecked())
                    if (Integer.parseInt(awayScore.getText().toString()) > Integer.parseInt(homeScore.getText().toString()) && Integer.parseInt(awayScore.getText().toString()) > 0)validate = true;
                if(homeGoal.isChecked())
                    if (Integer.parseInt(awayScore.getText().toString()) < Integer.parseInt(homeScore.getText().toString()) && Integer.parseInt(homeScore.getText().toString()) > 0)validate = true;

                if(awayScore.getText().length() > 0 && homeScore.getText().length() > 0 && validate){
                    //Away victory
                    if(awayGoal.isChecked()){
                        new BackgroundTaskHockeyEndGameTeams(HStartGamesActivity.bundle.getInt("id"),HStartGamesActivity.bundle.getInt("awayID"),1,0,0,0,Integer.parseInt(homeScore.getText().toString())).execute();
                        if(otCheck.isChecked())new BackgroundTaskHockeyEndGameTeams(HStartGamesActivity.bundle.getInt("id"),HStartGamesActivity.bundle.getInt("homeID"),0,0,1,0,Integer.parseInt(awayScore.getText().toString())).execute();
                        else if (soCheck.isChecked())new BackgroundTaskHockeyEndGameTeams(HStartGamesActivity.bundle.getInt("id"),HStartGamesActivity.bundle.getInt("homeID"),0,0,0,1,Integer.parseInt(awayScore.getText().toString())).execute();
                        else new BackgroundTaskHockeyEndGameTeams(HStartGamesActivity.bundle.getInt("id"),HStartGamesActivity.bundle.getInt("homeID"),0,1,0,0,Integer.parseInt(awayScore.getText().toString())).execute();
                    }
                    //Home victory
                    else{
                        new BackgroundTaskHockeyEndGameTeams(HStartGamesActivity.bundle.getInt("id"),HStartGamesActivity.bundle.getInt("homeID"),1,0,0,0,Integer.parseInt(awayScore.getText().toString())).execute();
                        if(otCheck.isChecked())new BackgroundTaskHockeyEndGameTeams(HStartGamesActivity.bundle.getInt("id"),HStartGamesActivity.bundle.getInt("awayID"),0,0,1,0,Integer.parseInt(homeScore.getText().toString())).execute();
                        else if (soCheck.isChecked())new BackgroundTaskHockeyEndGameTeams(HStartGamesActivity.bundle.getInt("id"),HStartGamesActivity.bundle.getInt("awayID"),0,0,0,1,Integer.parseInt(homeScore.getText().toString())).execute();
                        else new BackgroundTaskHockeyEndGameTeams(HStartGamesActivity.bundle.getInt("id"),HStartGamesActivity.bundle.getInt("awayID"),0,1,0,0,Integer.parseInt(homeScore.getText().toString())).execute();
                    }
                    startActivity(new Intent(HStartGamesActivity.context, HockeyMarqueur.class));
                    finish();
                }
                else Toast.makeText(HStartGamesActivity.context,"Le score final ne peut être égal",Toast.LENGTH_SHORT).show();
                builder1.dismiss();

            }

        });
        builder1.show();


    }


}

class BackgroundTaskHockeyEndGameTeams extends AsyncTask<Void,Void,String>{
    private int id;
    private int gameId;
    private String URLQuery;
    private HashMap<String,String> hMap = new HashMap<>();
    private int win;
    private int loses;
    private int otloses;
    private int soloses;
    private int ga;
    private String jsonString;


    BackgroundTaskHockeyEndGameTeams(int gameId,int id, int wins, int loses, int otloses, int soloses, int ga){
        this.gameId = gameId;
        this.id = id;
        this.win = wins;
        this.loses = loses;
        this.otloses = otloses;
        this.soloses = soloses;
        this.ga = ga;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_END_GAME;}

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",id + "");
        hMap.put("win",win + "");
        hMap.put("loses",loses + "");
        hMap.put("otloses",otloses+ "");
        hMap.put("soloses",soloses + "");
        hMap.put("ga",ga + "");
        hMap.put("gameId",gameId + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URLQuery,hMap);

        return jsonString;
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
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_END_PLAYERS;}

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
