package ml.statshub.statshub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class PlayersFromTeamsActivity extends AppCompatActivity {
    static  public Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_players_from_teams);
        bundle = getIntent().getExtras();
        Toast.makeText(this, bundle.getString("name"), Toast.LENGTH_LONG).show();
    }
}
