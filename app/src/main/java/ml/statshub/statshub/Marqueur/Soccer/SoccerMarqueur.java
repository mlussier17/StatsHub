package ml.statshub.statshub.Marqueur.Soccer;

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

public class SoccerMarqueur extends AppCompatActivity {
    public SGamesAdapter gAdapter;
    public List<Games> gamelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_marqueur);
        RecyclerView rView;
        gamelist = new ArrayList<Games>();
        rView = (RecyclerView)findViewById(R.id.soccerGames);
        rView.setLayoutManager(new LinearLayoutManager(this));
        gAdapter = new SGamesAdapter(this,gamelist);
        rView.setAdapter(gAdapter);
        new BackgroundTaskSoccerGames(this,gAdapter).execute();
    }
}

 class BackgroundTaskSoccerGames extends AsyncTask<Void,Void,String>{

     private AppCompatActivity c;
     private String URLQuery;
     private SGamesAdapter gAdapter;


     BackgroundTaskSoccerGames(AppCompatActivity c, SGamesAdapter adapt){
         this.c = c;
         this.gAdapter = adapt;
     }

     @Override
     protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_SOCCER_GAMES;}

     @Override
     protected String doInBackground(Void... params) {
         String jsonString;
         HTTPRequest request = new HTTPRequest();
         jsonString = request.getQueryToHDB(URLQuery);
         return jsonString;
     }

     @Override
     protected void onPostExecute(String s){
         try{
             JSONArray jArray = new JSONArray(s);
             for (int i = 0; i < jArray.length();i++){
                 JSONObject json_data = jArray.getJSONObject(i);
                 Games games = new Games();
                 games.setId(json_data.getInt("idSGames"));
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

                 ((SoccerMarqueur)c).gamelist.add(games);
             }

             gAdapter.notifyDataSetChanged();

         } catch (ParseException|JSONException e) {
             e.printStackTrace();
         }
     }
}
