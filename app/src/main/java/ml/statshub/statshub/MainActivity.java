package ml.statshub.statshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ml.statshub.statshub.Hockey.HockeyActivity;
import ml.statshub.statshub.Marqueur.MarqueurActivity;
import ml.statshub.statshub.Soccer.SoccerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button hockeyButton;
        Button soccerButton;
        Button marqueurButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Listener on the hockey button to redirect to the hockey activity
        hockeyButton = (Button)this.findViewById(R.id.Hockey);
        hockeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HockeyActivity.class);
                startActivity(i);
            }
        });

        //Listener on the soccer button to redirect to the soccer activity
        soccerButton = (Button)this.findViewById(R.id.Soccer);
        soccerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SoccerActivity.class);
                startActivity(i);
            }
        });

        //Listener on the soccer button to redirect to the marqueur activity
        marqueurButton = (Button)this.findViewById(R.id.Marqueur);
        marqueurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MarqueurActivity.class);
                startActivity(i);
            }
        });
    }
}
