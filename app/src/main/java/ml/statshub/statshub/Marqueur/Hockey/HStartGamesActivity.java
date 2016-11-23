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
import ml.statshub.statshub.Marqueur.Soccer.SStartGamesActivity;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;
import ml.statshub.statshub.Request.URLQuery;

import static ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_NUMBERS_PLAYERS;
import static ml.statshub.statshub.Request.URLQuery.URL_SOCCER_NUMBERS_PLAYERS;

public class HStartGamesActivity extends AppCompatActivity {
    static public Bundle bundle;
    static public Context context;
    private TextView away;
    private TextView home;
    public RadioGroup teams;
    static public EditText goal;
    static public EditText penalty;
    static public EditText editAssist1;
    static public EditText editAssist2;
    static public EditText editMInutes;
    public RadioButton awayGoal;
    public RadioButton homeGoal;
    public Button sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hstart_games);
        context = this.getApplicationContext();
        bundle = getIntent().getExtras();
        away = (TextView)findViewById(R.id.away);
        home = (TextView)findViewById(R.id.home);
        away.setText(bundle.getString("awayName"));
        home.setText(bundle.getString("homeName"));
        new BackgroundGetHockeyNumbersAway().execute();
        new BackgroundGetHockeyNumbersHome().execute();
    }

    public void addGoal(View v){
        final Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.hockeypopupgoal);
        teams = (RadioGroup) builder.findViewById(R.id.radioGroup);
        goal = (EditText)builder.findViewById(R.id.editGoal);
        awayGoal = (RadioButton)builder.findViewById(R.id.awayGoal);
        homeGoal = (RadioButton)builder.findViewById(R.id.homeGoal);
        editAssist1 = (EditText)builder.findViewById(R.id.editAssist1);
        editAssist2 = (EditText)builder.findViewById(R.id.editAssist2);
        sender = (Button)builder.findViewById(R.id.send);

        sender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String assist1 = editAssist1.getText().toString();
                String assist2 = editAssist2.getText().toString();
                //Add goal
                if(validate(1)){
                    if (awayGoal.isChecked())new BackgroundTaskHockeyPostGoal(HStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(goal.getText().toString())).execute();
                    else new BackgroundTaskHockeyPostGoal(HStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(goal.getText().toString())).execute();
                }
                //Add assist
                if(!assist1.isEmpty()) {
                    if (validate(3)) {
                        if (awayGoal.isChecked())
                            new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(goal.getText().toString())).execute();
                        else
                            new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(goal.getText().toString())).execute();
                    }
                }
                if(!assist2.isEmpty()) {
                    if (validate(3)) {
                        if (awayGoal.isChecked())
                            new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(goal.getText().toString())).execute();
                        else
                            new BackgroundTaskHockeyPostAssist(HStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(goal.getText().toString())).execute();
                    }
                }
                builder.dismiss();
            }
        });
        builder.show();
    }

    public Boolean validate(int id){
        int but;
        if(id == 1)
            but = Integer.parseInt(goal.getText().toString());
        else if(id == 2)
            but = Integer.parseInt(penalty.getText().toString());
        else
            but = Integer.parseInt(editAssist1.getText().toString());

        NumbersAvailable numero;
        numero = homeGoal.isChecked()? BackgroundGetHockeyNumbersHome.numbers: BackgroundGetHockeyNumbersAway.numbers;
        for (int i = 0; i < numero.getNumbers().size();i++){
            if( !(but == numero.getNumbers().get(i))) {
                Toast.makeText(this, "Le numéro n'est pas enregistré avec cettre équipe", Toast.LENGTH_SHORT).show();
                return false;
            }
            else return true;
        }
        return true;
    }

    public void addPenalty(View v){
        final Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.hockeypopuppenalty);

        teams = (RadioGroup) builder.findViewById(R.id.radioGroup);
        awayGoal = (RadioButton)builder.findViewById(R.id.awayPenalty);
        homeGoal = (RadioButton)builder.findViewById(R.id.homePenalty);
        penalty = (EditText)builder.findViewById(R.id.editPenalty);
        editMInutes = (EditText)builder.findViewById(R.id.editMinutes);
        sender = (Button)builder.findViewById(R.id.send);


        sender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(validate(2)){
                    Toast.makeText(HStartGamesActivity.this,"Good result",Toast.LENGTH_SHORT).show();
                    if (awayGoal.isChecked())new BackgroundTaskHockeyPostPenalty(bundle.getInt("awayID"), Integer.parseInt(penalty.getText().toString()),Integer.parseInt(editMInutes.getText().toString())).execute();
                    else new BackgroundTaskHockeyPostPenalty(bundle.getInt("homeID"), Integer.parseInt(penalty.getText().toString()),Integer.parseInt(editMInutes.getText().toString())).execute();
                }
                builder.dismiss();
            }
        });
        builder.show();
    }

    public void endOfGame(View v){

    }
}

class BackgroundTaskHockeyPostGoal extends AsyncTask<Void,Void,String> {
    private String URLQuery;
    private HashMap<String,String> hMap = new HashMap<>();
    private int teamid;
    private int number;
    public String jsonString;

    BackgroundTaskHockeyPostGoal(int id, int number){
        teamid = id;
        this.number = number;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_ADD_GOAL;}

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
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_ADD_GOAL;}

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",teamid + "");
        hMap.put("number",number + "");
        hMap.put("minutes",minutes + "");
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
