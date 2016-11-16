package ml.statshub.statshub.Marqueur.Soccer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ml.statshub.statshub.Class.Games;
import ml.statshub.statshub.Class.NumbersAvailable;
import ml.statshub.statshub.Hockey.PlayersFromTeamsActivity;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;

import static ml.statshub.statshub.Request.URLQuery.URL_SOCCER_NUMBERS_PLAYERS;

public class SStartGamesActivity extends AppCompatActivity {
    static public Bundle bundle;
    private TextView away;
    private TextView home;

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

    }

    public void addPenalty(View v){

    }

    public void endOfGame(View v){

    }
}

class BackgroundGetNumbersAway extends AsyncTask<Void,Void,String>{
    private String jsonString;
    private HashMap<String,String> hMap = new HashMap<>();

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
        NumbersAvailable numbers = new NumbersAvailable();
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
        NumbersAvailable numbers = new NumbersAvailable();
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
