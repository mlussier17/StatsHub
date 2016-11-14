package ml.statshub.statshub.Marqueur.Soccer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ml.statshub.statshub.R;

public class SStartGamesActivity extends AppCompatActivity {
    static private Bundle bundle;
    private TextView away;
    private TextView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sstart_games);
        bundle = getIntent().getExtras();
        away = (TextView)findViewById(R.id.away);
        home = (TextView)findViewById(R.id.home);
        away.setText(bundle.getString("awayName"));
        home.setText(bundle.getString("homeName"));
    }

    public void addGoal(View v){

    }

    public void addPenalty (View v){

    }

    public void endOfGame(View v){

    }
}
