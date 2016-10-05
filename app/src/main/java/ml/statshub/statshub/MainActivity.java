package ml.statshub.statshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button hockeyButton;
    private Button soccerButton;
    private Button marqueurButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Listener on the hockey button to redirect to the hockey activity
        this.hockeyButton = (Button)this.findViewById(R.id.Hockey);
        this.hockeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HockeyActivity.class);
                startActivity(i);
            }
        });

        //Listener on the soccer button to redirect to the soccer activity
        this.soccerButton = (Button)this.findViewById(R.id.Soccer);
        this.soccerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HockeyActivity.class);
                startActivity(i);
            }
        });

        //Listener on the soccer button to redirect to the soccer activity
        this.soccerButton = (Button)this.findViewById(R.id.Soccer);
        this.soccerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HockeyActivity.class);
                startActivity(i);
            }
        });
    }
}