package ml.statshub.statshub.Marqueur.Hockey;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ml.statshub.statshub.Class.Games;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;

public class HockeyMarqueur extends AppCompatActivity {
    private RecyclerView rView;
    public HGamesAdapter gAdapter;
    static public Context context;
    public List<Games> gamelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gamelist = new ArrayList<Games>();
        context = this.getApplicationContext();
        setContentView(R.layout.activity_hockey_marqueur);
        rView = (RecyclerView)findViewById(R.id.hockeyGames);
        rView.setLayoutManager(new LinearLayoutManager(this));
        gAdapter = new HGamesAdapter(this,gamelist);
        rView.setAdapter(gAdapter);
        new BackgroundTaskHockeyGames(this,rView,gAdapter).execute();
    }
}

class BackgroundTaskHockeyGames extends AsyncTask<Void,Void,String> {

    private RecyclerView rView;
    private AppCompatActivity c;
    private String URLQuery;
    private String jsonString;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private HGamesAdapter gAdapter;


    public BackgroundTaskHockeyGames(AppCompatActivity c,RecyclerView view, HGamesAdapter adapt){
        this.c = c;
        this.rView = view;
        this.gAdapter = adapt;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_GAMES;}

    @Override
    protected String doInBackground(Void... params) {
        HTTPRequest request = new HTTPRequest();
        jsonString = request.getQueryToHDB(URLQuery);
        return jsonString;
    }

    @Override
    protected void onPostExecute(String s){
        List<Games> hockeyGames = new ArrayList<>();
        try{
            JSONArray jArray = new JSONArray(s);
            for (int i = 0; i < jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Games games = new Games();
                games.setId(json_data.getInt("idHGames"));
                games.setAway(json_data.getInt("idAwayTeams"));
                games.setAwayName(json_data.getString("AwayName"));
                games.setHome(json_data.getInt("idHomeTeams"));
                games.setHomeName(json_data.getString("HomeName"));
                games.setLocation(json_data.getString("Location"));
                String date = json_data.getString("Time");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CANADA_FRENCH);
                Date newDate = format.parse(date);
                games.setDate(format.format(newDate));
                games.setEnded(json_data.getInt("IsEnded"));
                ((HockeyMarqueur)c).gamelist.add(games);
            }
            gAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
