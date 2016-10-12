package ml.statshub.statshub;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LeagueFragment extends Fragment{


    private HTTPRequest query;


    public LeagueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new BackgroundTask((AppCompatActivity)getActivity()).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_league, container, false);
    }

}

class BackgroundTask extends AsyncTask<Void,Void,String>{

    public BackgroundTask(AppCompatActivity c){
        _c = c;
    }
    private AppCompatActivity _c;
    private URLQuery links;
    private String jsonString;
    String jsonUrl;
    private RecyclerView leaguesLists;
    private LeaguesAdapter lAdapter;

    @Override
    protected void onPreExecute() {
            jsonUrl = links.URL_LIST_HOCKEY_LEAGUES;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((jsonString = reader.readLine()) != null) {
                stringBuilder.append(jsonString+"\n");
            }
            reader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return stringBuilder.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        List<Leagues> leaguesList = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Leagues ligues = new Leagues();
                ligues._idLeagues = json_data.getInt("idHLeagues");
                ligues._leagueName= json_data.getString("Name");
                //ligues._nbTeams = json_data.getInt("NbTeam");
                leaguesList.add(ligues);
            }
            leaguesLists = (RecyclerView)_c.findViewById(R.id.ListLeagues);
            lAdapter = new LeaguesAdapter(_c,leaguesList);
            leaguesLists.setAdapter(lAdapter);
            leaguesLists.setLayoutManager(new LinearLayoutManager(_c));

            //TextView textView = (TextView) _c.findViewById(R.id.ListLeagues);
            //textView.setText(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}