package ml.statshub.statshub.Marqueur.Soccer;

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
import ml.statshub.statshub.Marqueur.Hockey.Results;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;

import static ml.statshub.statshub.R.id.awayResult;
import static ml.statshub.statshub.Request.URLQuery.URL_SOCCER_NUMBERS_PLAYERS;

public class SStartGamesActivity extends AppCompatActivity {
    static public Bundle bundle;
    static public Context context;
    private TextView away;
    private TextView home;
    public RadioGroup teams;
    static public EditText goal;
    static public EditText card;
    public RadioButton awayGoal;
    public RadioButton homeGoal;
    public RadioButton yellowCard;
    public RadioButton redCard;
    public Button sender;
    public TextView awayScore;
    public TextView homeScore;
    public Results results;
    private TextView awayResultText;
    private TextView homeResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_sstart_games);
        results = new Results();
        results.setAway(0);
        results.setHome(0);
        bundle = getIntent().getExtras();
        away = (TextView)findViewById(R.id.away);
        home = (TextView)findViewById(R.id.home);
        away.setText(bundle.getString("awayName"));
        home.setText(bundle.getString("homeName"));
        awayResultText = (TextView)findViewById(awayResult);
        homeResultText = (TextView)findViewById(R.id.homeResult);
        new BackgroundGetNumbersAway().execute();
        new BackgroundGetNumbersHome().execute();
        awayResultText.setText(results.getAway() + "");
        homeResultText.setText(results.getHome() + "");
    }

    public void addGoal(View v){
        final Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.soccerpopupgoal);

        teams = (RadioGroup) builder.findViewById(R.id.radioGroup);
        goal = (EditText)builder.findViewById(R.id.editGoal);
        awayGoal = (RadioButton)builder.findViewById(R.id.awayGoal);
        homeGoal = (RadioButton)builder.findViewById(R.id.homeGoal);
        sender = (Button)builder.findViewById(R.id.send);


        sender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(goal.getText().length() > 0) {
                    if (validate(1)) {
                        Toast.makeText(SStartGamesActivity.this, "Good result", Toast.LENGTH_SHORT).show();
                        if (awayGoal.isChecked()) {
                            new BackgroundTaskSoccerPostGoal(SStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(SStartGamesActivity.goal.getText().toString())).execute();
                            results.setAway(1);
                            awayResultText.setText(results.getAway() + "");
                        } else {
                            new BackgroundTaskSoccerPostGoal(SStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(SStartGamesActivity.goal.getText().toString())).execute();
                            results.setHome(1);
                            homeResultText.setText(results.getHome() + "");
                        }
                    }
                }
                else Toast.makeText(SStartGamesActivity.context,"Le but est vide", Toast.LENGTH_SHORT).show();

                builder.dismiss();
            }
        });
        builder.show();

    }

    public Boolean validate(int id){
        int but;
        if(id == 1)
             but = Integer.parseInt(goal.getText().toString());
        else
            but = Integer.parseInt(card.getText().toString());

        int equipe;
        NumbersAvailable numero;
        equipe = homeGoal.isChecked()? bundle.getInt("homeID"):bundle.getInt("awayID");
        numero = homeGoal.isChecked()? BackgroundGetNumbersHome.numbers:BackgroundGetNumbersAway.numbers;
        for (int i = 0; i < numero.getNumbers().size();i++){
            if( !(but == numero.getNumbers().get(i))) {
                Toast.makeText(this, "Le numéro n'est pas enregistré avec cettre équipe", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void addCard(View v){
        final Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.soccerpopupcard);

        teams = (RadioGroup) builder.findViewById(R.id.radioGroup);
        awayGoal = (RadioButton)builder.findViewById(R.id.awayGoal);
        homeGoal = (RadioButton)builder.findViewById(R.id.homeGoal);
        card = (EditText)builder.findViewById(R.id.editCard);
        yellowCard = (RadioButton)builder.findViewById(R.id.yellowCard);
        redCard = (RadioButton)builder.findViewById(R.id.redCard);
        sender = (Button)builder.findViewById(R.id.send);


        sender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(card.getText().length() > 0) {
                    if (validate(2)) {
                        Toast.makeText(SStartGamesActivity.this, "Good result", Toast.LENGTH_SHORT).show();
                        if (awayGoal.isChecked()) {
                            if (yellowCard.isChecked())
                                new BackgroundTaskSoccerPostCard(SStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(SStartGamesActivity.card.getText().toString()), "yellow").execute();
                            else
                                new BackgroundTaskSoccerPostCard(SStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(SStartGamesActivity.card.getText().toString()), "red").execute();
                        } else {
                            if (yellowCard.isChecked())
                                new BackgroundTaskSoccerPostCard(SStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(SStartGamesActivity.card.getText().toString()), "yellow").execute();
                            else
                                new BackgroundTaskSoccerPostCard(SStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(SStartGamesActivity.card.getText().toString()), "red").execute();
                        }
                    }
                }
                else Toast.makeText(SStartGamesActivity.context,"Le carton est vide", Toast.LENGTH_SHORT).show();

                builder.dismiss();

            }
        });
        builder.show();

    }

    public void endOfGame(View v){


        final NumbersAvailable numeroAway;
        final NumbersAvailable numeroHome;
        numeroAway = BackgroundGetNumbersAway.numbers;
        numeroHome = BackgroundGetNumbersHome.numbers;

        final Dialog builder1 = new Dialog(this);
        builder1.setContentView(R.layout.soccerpopupend);
        awayScore = (TextView)builder1.findViewById(R.id.editAwayResult);
        homeScore= (TextView)builder1.findViewById(R.id.editHomeResult);
        awayGoal = (RadioButton)builder1.findViewById(R.id.awayGoal);
        homeGoal = (RadioButton)builder1.findViewById(R.id.homeGoal);
        sender = (Button)builder1.findViewById(R.id.send);
        awayScore.setText(results.getAway() + "");
        homeScore.setText(results.getHome() + "");
        if(results.getAway() > results.getHome())awayGoal.setChecked(true);
        else if(results.getAway() < results.getHome()) homeGoal.setChecked(true);
        else{
            awayGoal.setChecked(false);
            homeGoal.setChecked(false);
        }
        awayGoal.setEnabled(false);
        homeGoal.setEnabled(false);


        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(awayScore.getText().length() > 0 && homeScore.getText().length() > 0){
                    //Away victory
                    if(awayGoal.isChecked()){
                        new BackgroundTaskSoccerEndGameTeams(SStartGamesActivity.bundle.getInt("id"),SStartGamesActivity.bundle.getInt("awayID"),1,0,0,Integer.parseInt(homeScore.getText().toString())).execute();
                        new BackgroundTaskSoccerEndGameTeams(SStartGamesActivity.bundle.getInt("id"),SStartGamesActivity.bundle.getInt("homeID"),0,1,0,Integer.parseInt(awayScore.getText().toString())).execute();
                    }
                    //Home victory
                    else if (homeGoal.isChecked()){
                        new BackgroundTaskSoccerEndGameTeams(SStartGamesActivity.bundle.getInt("id"),SStartGamesActivity.bundle.getInt("homeID"),1,0,0,Integer.parseInt(awayScore.getText().toString())).execute();
                        new BackgroundTaskSoccerEndGameTeams(SStartGamesActivity.bundle.getInt("id"),SStartGamesActivity.bundle.getInt("awayID"),0,1,0,Integer.parseInt(homeScore.getText().toString())).execute();
                    }
                    //Ties
                    else{
                        new BackgroundTaskSoccerEndGameTeams(SStartGamesActivity.bundle.getInt("id"),SStartGamesActivity.bundle.getInt("homeID"),0,0,1,Integer.parseInt(awayScore.getText().toString())).execute();
                        new BackgroundTaskSoccerEndGameTeams(SStartGamesActivity.bundle.getInt("id"),SStartGamesActivity.bundle.getInt("awayID"),0,0,1,Integer.parseInt(homeScore.getText().toString())).execute();
                    }
                    new BackgroundTaskSoccerEndGamePlayers(SStartGamesActivity.bundle.getInt("awayID"), numeroAway).execute();
                    new BackgroundTaskSoccerEndGamePlayers(SStartGamesActivity.bundle.getInt("homeID"), numeroHome).execute();
                    startActivity(new Intent(SStartGamesActivity.context, SoccerMarqueur.class));
                    finish();
                }
                else Toast.makeText(SStartGamesActivity.context,"Le score final est invalide",Toast.LENGTH_SHORT).show();
                builder1.dismiss();

            }

        });
        builder1.show();
    }
}

class BackgroundTaskSoccerEndGameTeams extends AsyncTask<Void,Void,String>{
    private int id;
    private int gameId;
    private String URLQuery;
    private HashMap<String,String> hMap = new HashMap<>();
    private int win;
    private int loses;
    private int ties;
    private int soloses;
    private int ga;
    private String jsonString;


    BackgroundTaskSoccerEndGameTeams(int gameId,int id, int wins, int loses, int ties, int ga){
        this.gameId = gameId;
        this.id = id;
        this.win = wins;
        this.loses = loses;
        this.ties = ties;
        this.ga = ga;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_SOCCER_END_GAME;}

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",id + "");
        hMap.put("win",win + "");
        hMap.put("loses",loses + "");
        hMap.put("ties",ties+ "");
        hMap.put("ga",ga + "");
        hMap.put("gameId",gameId + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URLQuery,hMap);

        return jsonString;
    }
}

class BackgroundTaskSoccerEndGamePlayers extends AsyncTask<Void,Void,String>{

    private String URLQuery;
    private int teamId;
    private String jsonString;
    private HashMap<String,String> hMap = new HashMap<>();
    NumbersAvailable numero;

    BackgroundTaskSoccerEndGamePlayers(int teamId, NumbersAvailable numero){
        this.teamId = teamId;
        this.numero = numero;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_SOCCER_END_PLAYERS;}

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

class BackgroundTaskSoccerPostGoal extends AsyncTask<Void,Void,String> {
    private String URLQuery;
    private HashMap<String,String> hMap = new HashMap<>();
    private int teamid;
    private int number;
    public String jsonString;

    BackgroundTaskSoccerPostGoal(int id, int number){
        teamid = id;
        this.number = number;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_SOCCER_ADD_GOAL;}

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",teamid + "");
        hMap.put("number",number + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URLQuery,hMap);

        return jsonString;
    }
}

class BackgroundTaskSoccerPostCard extends AsyncTask<Void,Void,String> {
    private String URLQuery;
    private HashMap<String,String> hMap = new HashMap<>();
    private int teamid;
    private int number;
    public String jsonString;
    public String card;

    BackgroundTaskSoccerPostCard(int id, int number, String cards){
        teamid = id;
        this.number = number;
        card = cards;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_SOCCER_ADD_CARD;}

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",teamid + "");
        hMap.put("number",number + "");
        hMap.put("card",card);
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URLQuery,hMap);

        return jsonString;
    }
}


class BackgroundGetNumbersAway extends AsyncTask<Void,Void,String>{
    private String jsonString;
    private HashMap<String,String> hMap = new HashMap<>();
    static public NumbersAvailable numbers;

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id", SStartGamesActivity.bundle.getInt("awayID") + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URL_SOCCER_NUMBERS_PLAYERS,hMap);
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
            numbers.setIdGames(SStartGamesActivity.bundle.getInt("id"));
            numbers.setIdTeams(SStartGamesActivity.bundle.getInt("awayID"));
            numbers.setNumbers(number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

class BackgroundGetNumbersHome extends AsyncTask<Void,Void,String>{
    private String jsonString;
    private HashMap<String,String> hMap = new HashMap<>();
    static public  NumbersAvailable numbers;

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id", SStartGamesActivity.bundle.getInt("homeID") + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URL_SOCCER_NUMBERS_PLAYERS,hMap);
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
            numbers.setIdGames(SStartGamesActivity.bundle.getInt("id"));
            numbers.setIdTeams(SStartGamesActivity.bundle.getInt("homeID"));
            numbers.setNumbers(number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

