package ml.statshub.statshub;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarqueurActivity extends AppCompatActivity {

    EditText users;
    EditText password;
    String login = null;
    String pass = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marqueur);

    }

    public void connect(View view){
        users = (EditText)findViewById(R.id.users);
        password = (EditText)findViewById(R.id.password);
        pass = password.getText().toString();
        login = users.getText().toString();
        new BackgroundTaskLogin(this,login,pass).execute();
    }
}

class BackgroundTaskLogin extends AsyncTask<Void,Void,String> {

    public BackgroundTaskLogin(AppCompatActivity c, String _login, String _password){
        _c = c;
        login = _login;
        password = _password;
    }
    private AppCompatActivity _c;
    private String jsonString;
    private String login;
    private String password;
    String jsonUrl;
    private HashMap<String,String> hMap = new HashMap<>();

    @Override
    protected void onPreExecute() {jsonUrl = URLQuery.URL_LOGIN;}

    @Override
    protected String doInBackground(Void... params) {
        hMap.put("login",login);
        hMap.put("pwd", password);
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(jsonUrl,hMap);

        return jsonString;
    }


    @Override
    protected void onProgressUpdate(Void... values) {super.onProgressUpdate(values);}

    @Override
    protected void onPostExecute(String s) {
        int login;
        try {
            JSONObject json_data = new JSONObject(s);
            login = json_data.getInt("logged");
            if (login == 0){
                Toast.makeText(_c,"Usager/Mot de passe invalide",Toast.LENGTH_SHORT).show();
            }
            if (login == 1){
                Intent i = new Intent(_c,LoggedMarqueurActivity.class);
                _c.startActivity(i);
            }
            //lAdapter = new LeaguesAdapter(_c,leaguesList);
            //leaguesLists.setAdapter(lAdapter);

            //lAdapter.notifyDataSetChanged();

            //TextView textView = (TextView) _c.findViewById(R.id.ListLeagues);
            //textView.setText(leaguesList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
