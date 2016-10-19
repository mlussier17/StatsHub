package ml.statshub.statshub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by 196128636 on 2016-10-19.
 */

public class PlayersTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Players> data = Collections.emptyList();

    public PlayersTeamsAdapter(Context c, List<Players> players) {
        context = c;
        inflater = LayoutInflater.from(context);
        data = players;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.layoutplayersteams, parent,false);
        return new PlayersTeamsAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        PlayersTeamsAdapter.MyHolder myHolder= (PlayersTeamsAdapter.MyHolder) holder;
        final Players current = data.get(position);
        myHolder.textName.setText(current.getName());
        myHolder.textFName.setText(current.getFName());
        myHolder.textGP.setText(current.getGP() + "");
        myHolder.textG.setText(current.getGoals() + "");
        myHolder.textA.setText(current.getAssists() + "");
        myHolder.textPts.setText(current.getPts() + "");
        myHolder.textPPG.setText(current.getPPG() + "");
        myHolder.textPKG.setText(current.getPKG() + "");
        myHolder.textPen.setText(current.getPen() + "");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView textName;
        TextView textFName;
        TextView textGP;
        TextView textG;
        TextView textA;
        TextView textPts;
        TextView textPPG;
        TextView textPKG;
        TextView textPen;

        TextView textSize;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.playerName);
            textFName= (TextView) itemView.findViewById(R.id.playerFName);
            textGP= (TextView) itemView.findViewById(R.id.playerGP);
            textG= (TextView) itemView.findViewById(R.id.playerGoals);
            textA= (TextView) itemView.findViewById(R.id.playerAssists);
            textPts= (TextView) itemView.findViewById(R.id.playerPoints);
            textPPG= (TextView) itemView.findViewById(R.id.playerPPG);
            textPKG= (TextView) itemView.findViewById(R.id.playerPKG);
            textPen= (TextView) itemView.findViewById(R.id.playerPen);
        }

    }
}
