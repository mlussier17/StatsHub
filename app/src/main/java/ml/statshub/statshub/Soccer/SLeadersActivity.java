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

import ml.statshub.statshub.Class.Players;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;
import ml.statshub.statshub.Request.URLQuery;

public class SLeadersActivity extends AppCompatActivity {
    static  public Bundle bundle;
    private RecyclerView rView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleaders);
        bundle = getIntent().getExtras();
        TextView test;
        test = (TextView)findViewById(R.id.showPlayers);
        test.setText(bundle.getString("name"));
        rView = (RecyclerView)findViewById(R.id.listPlayers);
        rView.setLayoutManager(new LinearLayoutManager(this));
        new BackgroundTask10(this, rView).execute();
    }
}
class BackgroundTask10 extends AsyncTask<Void,Void,String> {

    public BackgroundTask10(AppCompatActivity c, RecyclerView vue){
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
        jsonUrl = URLQuery.URL_LIST_SOCCER_LEADERS_PER_LEAGUES;
    }

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("id",SLeadersActivity.bundle.getInt("id") + "");
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
        SPlayersLeadersAdapter pAdapter;
        List<Players> playersList = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Players players = new Players();
                players.setFName(json_data.getString("FirstName"));
                players.setName(json_data.getString("LastName"));
                players.setTeam(json_data.getString("NomEquipe"));
                players.setNumber(json_data.getInt("Number"));
                players.setGP(json_data.getInt("NbGP"));
                players.setGoals(json_data.getInt("NbGoals"));
                players.setYC(json_data.getInt("NbYellowCards"));
                players.setRC(json_data.getInt("NbRedCards"));
                playersList.add(players);
            }
            pAdapter = new SPlayersLeadersAdapter(_c,playersList);
            playersLists.setAdapter(pAdapter);

            pAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
