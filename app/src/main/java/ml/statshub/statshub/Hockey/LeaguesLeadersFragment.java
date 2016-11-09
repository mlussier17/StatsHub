package ml.statshub.statshub.Hockey;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ml.statshub.statshub.Class.Leagues;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;
import ml.statshub.statshub.Request.URLQuery;

public class LeaguesLeadersFragment extends Fragment {
    RecyclerView rView;
    public LeaguesLeadersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaguesleaders, container, false);
        rView = (RecyclerView)view.findViewById(R.id.ListLeagues);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new BackgroundTask7((AppCompatActivity)getActivity(), rView).execute();
        return view;
    }

}

class BackgroundTask7 extends AsyncTask<Void,Void,String> {

    public BackgroundTask7(AppCompatActivity c, RecyclerView vue){
        _c = c;
        leaguesLists = vue;
    }
    private AppCompatActivity _c;
    private String jsonString;
    String jsonUrl;
    private RecyclerView leaguesLists;
    private LeaguesLeaderAdapter lAdapter;

    @Override
    protected void onPreExecute() {jsonUrl = URLQuery.URL_LIST_HOCKEY_LEAGUES;}

    @Override
    protected String doInBackground(Void... params) {
        HTTPRequest request = new HTTPRequest();
        jsonString = request.getQueryToHDB(jsonUrl);

        return jsonString;
    }


    @Override
    protected void onProgressUpdate(Void... values) {super.onProgressUpdate(values);}

    @Override
    protected void onPostExecute(String s) {
        List<Leagues> leaguesList = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Leagues ligues = new Leagues();
                ligues.setId(json_data.getInt("idHLeagues"));
                ligues.setName(json_data.getString("Name"));
                leaguesList.add(ligues);
            }
            lAdapter = new LeaguesLeaderAdapter(_c,leaguesList);
            leaguesLists.setAdapter(lAdapter);

            lAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
