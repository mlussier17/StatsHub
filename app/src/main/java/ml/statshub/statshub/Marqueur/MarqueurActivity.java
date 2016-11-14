package ml.statshub.statshub.Marqueur;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ml.statshub.statshub.Marqueur.Hockey.HockeyMarqueur;
import ml.statshub.statshub.Marqueur.Soccer.SoccerMarqueur;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;
import ml.statshub.statshub.Request.URLQuery;

public class MarqueurActivity extends AppCompatActivity {

    EditText users;
    EditText password;
    RadioButton bHockey;
    RadioButton bSoccer;
    String login = null;
    String pass = null;
    Boolean hockey = false;
    Boolean soccer = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marqueur);

    }

    public void connect(View view){
        users = (EditText)findViewById(R.id.users);
        password = (EditText)findViewById(R.id.password);
        bHockey = (RadioButton)findViewById(R.id.rbHockey);
        bSoccer = (RadioButton)findViewById(R.id.rbSoccer);
        pass = password.getText().toString();
        login = users.getText().toString();
        hockey = bHockey.isChecked();
        soccer = bSoccer.isChecked();
        new BackgroundTaskLogin(this,login,pass,hockey,soccer).execute();
    }
}

class BackgroundTaskLogin extends AsyncTask<Void,Void,String> {

    public BackgroundTaskLogin(AppCompatActivity c, String _login, String _password,Boolean hockey, Boolean soccer){
        _c = c;
        login = _login;
        password = _password;
        this.hockey = hockey;
        this.soccer = soccer;
    }
    private AppCompatActivity _c;
    private Boolean hockey;
    private Boolean soccer;
    private String jsonString;
    private String login;
    private String password;
    String jsonUrl;
    private HashMap<String,String> hMap = new HashMap<>();

    @Override
    protected void onPreExecute() {jsonUrl =(hockey? URLQuery.URL_HOCKEY_LOGIN : URLQuery.URL_SOCCER_LOGIN);}

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
            if ((login = json_data.getInt("logged")) == 0) Toast.makeText(_c,"Usager/Mot de passe invalide",Toast.LENGTH_SHORT).show();
            if (login == 1) _c.startActivity((hockey ? new Intent(_c, HockeyMarqueur.class) : new Intent(_c, SoccerMarqueur.class)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
