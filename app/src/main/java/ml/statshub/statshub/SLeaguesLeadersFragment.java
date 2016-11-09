package ml.statshub.statshub;

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

public class SLeaguesLeadersFragment extends Fragment {
    RecyclerView rView;
    public SLeaguesLeadersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sleagues_leaders, container, false);
        rView = (RecyclerView)view.findViewById(R.id.ListLeagues);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new BackgroundTask9((AppCompatActivity)getActivity(), rView).execute();
        return view;
    }

}

class BackgroundTask9 extends AsyncTask<Void,Void,String> {

    public BackgroundTask9(AppCompatActivity c, RecyclerView vue){
        _c = c;
        leaguesLists = vue;
    }
    private AppCompatActivity _c;
    private String jsonString;
    String jsonUrl;
    private RecyclerView leaguesLists;
    private SLeaguesLeaderAdapter lAdapter;

    @Override
    protected void onPreExecute() {jsonUrl = URLQuery.URL_LIST_SOCCER_LEAGUES;}

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
                ligues.setId(json_data.getInt("idSLeagues"));
                ligues.setName(json_data.getString("Name"));
                leaguesList.add(ligues);
            }
            lAdapter = new SLeaguesLeaderAdapter(_c,leaguesList);
            leaguesLists.setAdapter(lAdapter);

            lAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
