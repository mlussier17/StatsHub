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
 * Created by 196128636 on 2016-10-12.
 */

public class STeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Teams> data = Collections.emptyList();

    public STeamsAdapter(Context c, List<Teams> teams) {
        context = c;
        inflater = LayoutInflater.from(context);
        data = teams;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.layoutteams, parent,false);
        return new STeamsAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        STeamsAdapter.MyHolder myHolder= (STeamsAdapter.MyHolder) holder;
        final Teams current = data.get(position);
        myHolder.textName.setText(current.getName());
        myHolder.textGP.setText(current.getGP() + "");
        myHolder.textWins.setText(current.getWins() + "");
        myHolder.textLoses.setText(current.getLoses() + "");
        myHolder.textOTLoses.setText(current.getTies() + "");
        myHolder.textPoints.setText(current.getPoints() + "");
        myHolder.textGF.setText(current.getGF() + "");
        myHolder.textGA.setText(current.getGA() + "");

        myHolder.textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PlayersFromTeamsActivity.class);
                Bundle b = new Bundle();
                b.putInt("id",current.getId());
                b.putString("name",current.getName());
                i.putExtras(b);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView textName;
        TextView textGP;
        TextView textWins;
        TextView textLoses;
        TextView textOTLoses;
        TextView textSOLoses;
        TextView textPoints;
        TextView textGF;
        TextView textGA;

        TextView textSize;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.teamName);
            textWins= (TextView) itemView.findViewById(R.id.teamWins);
            textGP= (TextView) itemView.findViewById(R.id.teamGP);
            textLoses= (TextView) itemView.findViewById(R.id.teamLoses);
            textOTLoses= (TextView) itemView.findViewById(R.id.teamOT);
            textSOLoses= (TextView) itemView.findViewById(R.id.teamSO);
            textPoints= (TextView) itemView.findViewById(R.id.teamPts);
            textGF= (TextView) itemView.findViewById(R.id.teamGF);
            textGA= (TextView) itemView.findViewById(R.id.teamGA);
        }

    }
}
