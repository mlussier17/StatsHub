package ml.statshub.statshub.Marqueur.Hockey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collections;
import java.util.List;
import ml.statshub.statshub.Class.Games;
import ml.statshub.statshub.R;

/**
 * Created by 196128636 on 2016-11-14.
 */

public class HGamesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<Games> data;

    public HGamesAdapter(Context c, List<Games> games){
        this.context = c;
        this.inflater = LayoutInflater.from(this.context);
        this.data = games;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.layouthockeygames, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        final Games current = data.get(position);
        myHolder.textAway.setText(current.getAwayName());
        myHolder.textHome.setText(current.getHomeName());
        myHolder.textLocation.setText(current.getLocation());
        myHolder.textDate.setText(current.getDate() + "");

        myHolder.rLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getEnded() == 0) {
                    Intent i = new Intent(context, HStartGamesActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("id", current.getId());
                    b.putInt("awayID", current.getAway());
                    b.putString("awayName", current.getAwayName());
                    b.putInt("homeID", current.getHome());
                    b.putString("homeName", current.getHomeName());
                    i.putExtras(b);
                    context.startActivity(i);
                }
                else Toast.makeText(HockeyMarqueur.context,"La partie est terminée",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {return data.size();}

    class MyHolder extends RecyclerView.ViewHolder{
        RelativeLayout rLayout;

        TextView textAway;
        TextView textHome;
        TextView textLocation;
        TextView textDate;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            rLayout = (RelativeLayout)itemView.findViewById(R.id.HListGame);
            textAway= (TextView) itemView.findViewById(R.id.awayTeam);
            textHome= (TextView) itemView.findViewById(R.id.homeTeam);
            textLocation= (TextView) itemView.findViewById(R.id.location);
            textDate= (TextView) itemView.findViewById(R.id.date);
        }

    }
}
