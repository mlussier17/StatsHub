package ml.statshub.statshub.Hockey;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ml.statshub.statshub.Class.Players;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;
import ml.statshub.statshub.Request.URLQuery;

public class PlayersFromTeamsActivity extends AppCompatActivity {
    static  public Bundle bundle;
    private RecyclerView rView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        setContentView(R.layout.activity_players_from_teams);
        TextView test;
        test = (TextView)findViewById(R.id.showPlayers);
        test.setText(bundle.getString("name"));
        rView = (RecyclerView)findViewById(R.id.listPlayers);
        rView.setLayoutManager(new LinearLayoutManager(this));
        new BackgroundTask2(this, rView).execute();
    }
}
class BackgroundTask2 extends AsyncTask<Void,Void,String> {

    public BackgroundTask2(AppCompatActivity c, RecyclerView vue){
        _c = c;
        playersLists = vue;
    }
    private AppCompatActivity _c;
    private String jsonString;
    private String jsonUrl;
    private RecyclerView playersLists;
    private HashMap<String,String> hMap = new HashMap<>();

    @Override
    protected void onPreExecute() {
        jsonUrl = URLQuery.URL_LIST_HOCKEY_PLAYERS_FROM_TEAMS;
    }

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",PlayersFromTeamsActivity.bundle.getInt("id") + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(jsonUrl,hMap);

        return jsonString;
    }

    @Override
    protected void onPostExecute(String s) {
        PlayersTeamsAdapter pAdapter;
        List<Players> playersList = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Players players = new Players();
                players.setId(json_data.getInt("idHPlayers"));
                players.setFName(json_data.getString("FirstName"));
                players.setName(json_data.getString("LastName"));
                players.setNumber(json_data.getInt("Number"));
                players.setGP(json_data.getInt("NbGP"));
                players.setGoals(json_data.getInt("NbGoals"));
                players.setAssists(json_data.getInt("NbAssists"));
                players.setPoints(json_data.getInt("TotalPoints"));
                players.setPPG(json_data.getInt("NbPPGoals"));
                players.setPKG(json_data.getInt("NbPKGoals"));
                players.setPenalty(json_data.getInt("NbPenaltyMinutes"));
                playersList.add(players);
            }
            pAdapter = new PlayersTeamsAdapter(_c,playersList);
            playersLists.setAdapter(pAdapter);

            pAdapter.notifyDataSetChanged();

            //TextView textView = (TextView) _c.findViewById(R.id.ListLeagues);
            //textView.setText(leaguesList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
