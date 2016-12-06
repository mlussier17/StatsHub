package ml.statshub.statshub.Soccer;

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

import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;
import ml.statshub.statshub.Request.URLQuery;
import ml.statshub.statshub.Class.Teams;

public class STeamsActivity extends AppCompatActivity {
    static  public Bundle bundle;
    private RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        setContentView(R.layout.activity_steams);
        TextView test;
        test = (TextView)findViewById(R.id.showTeams);
        test.setText(bundle.getString("name"));
        rView = (RecyclerView)findViewById(R.id.listTeams);
        rView.setLayoutManager(new LinearLayoutManager(this));
        new BackgroundTask5(this, rView).execute();
    }

    static public Bundle getBundle() {return bundle;}
}
class BackgroundTask5 extends AsyncTask<Void,Void,String> {

    public BackgroundTask5(AppCompatActivity c, RecyclerView vue){
        _c = c;
        teamsLists = vue;
    }
    private AppCompatActivity _c;
    private String jsonString;
    private String jsonUrl;
    private RecyclerView teamsLists;

    private HashMap<String,String> hMap = new HashMap<>();

    @Override
    protected void onPreExecute() {
        jsonUrl = URLQuery.URL_LIST_SOCCER_TEAMS_FROM_LEAGUES;
    }

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",STeamsActivity.getBundle().getInt("id") + "");
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(jsonUrl,hMap);

        return jsonString;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        STeamsAdapter tAdapter;
        List<Teams> teamsList = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Teams teams = new Teams();
                teams.setId(json_data.getInt("idSTeams"));
                teams.setTeams(json_data.getString("Name"));
                teams.setNbGP(json_data.getInt("NbGP"));
                teams.setWins(json_data.getInt("NbWins"));
                teams.setLoses(json_data.getInt("NbLoses"));
                teams.setTies(json_data.getInt("NbTies"));
                teams.setPoints(json_data.getInt("NbPoints"));
                teams.setGF(json_data.getInt("Total"));
                teams.setGA(json_data.getInt("NbGoalsAgainst"));
                //teams.setYC(json_data.getInt("NbGoalsAgainst"));
                //teams.setRC(json_data.getInt("NbGoalsAgainst"));
                teamsList.add(teams);
            }
            tAdapter = new STeamsAdapter(_c,teamsList);
            teamsLists.setAdapter(tAdapter);

            tAdapter.notifyDataSetChanged();

            //TextView textView = (TextView) _c.findViewById(R.id.ListLeagues);
            //textView.setText(leaguesList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
