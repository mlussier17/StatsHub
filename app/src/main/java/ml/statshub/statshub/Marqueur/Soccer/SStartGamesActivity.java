package ml.statshub.statshub.Marqueur.Soccer;

import android.app.Dialog;
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

import static ml.statshub.statshub.Request.URLQuery.URL_SOCCER_NUMBERS_PLAYERS;

public class SStartGamesActivity extends AppCompatActivity {
    static public Bundle bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sstart_games);
        bundle = getIntent().getExtras();
        away = (TextView)findViewById(R.id.away);
        home = (TextView)findViewById(R.id.home);
        away.setText(bundle.getString("awayName"));
        home.setText(bundle.getString("homeName"));
        new BackgroundGetNumbersAway().execute();
        new BackgroundGetNumbersHome().execute();
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
                if(validate(1)){
                    Toast.makeText(SStartGamesActivity.this,"Good result",Toast.LENGTH_SHORT).show();
                    if (awayGoal.isChecked())new BackgroundTaskSoccerPostGoal(SStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(SStartGamesActivity.goal.getText().toString())).execute();
                    else new BackgroundTaskSoccerPostGoal(SStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(SStartGamesActivity.goal.getText().toString())).execute();
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
                if(validate(2)){
                    Toast.makeText(SStartGamesActivity.this,"Good result",Toast.LENGTH_SHORT).show();
                    if (awayGoal.isChecked()){
                        if(yellowCard.isChecked())
                            new BackgroundTaskSoccerPostCard(SStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(SStartGamesActivity.card.getText().toString()),"yellow").execute();
                        else
                            new BackgroundTaskSoccerPostCard(SStartGamesActivity.bundle.getInt("awayID"), Integer.parseInt(SStartGamesActivity.card.getText().toString()),"red").execute();
                    }
                    else {
                        if(yellowCard.isChecked())
                            new BackgroundTaskSoccerPostCard(SStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(SStartGamesActivity.card.getText().toString()),"yellow").execute();
                        else
                            new BackgroundTaskSoccerPostCard(SStartGamesActivity.bundle.getInt("homeID"), Integer.parseInt(SStartGamesActivity.card.getText().toString()),"red").execute();
                    }
                }

                builder.dismiss();

            }
        });
        builder.show();

    }

    public void endOfGame(View v){

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
