package ml.statshub.statshub.Soccer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ml.statshub.statshub.Class.Players;
import ml.statshub.statshub.R;

/**
 * Created by 196128636 on 2016-10-19.
 */

public class SPlayersTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Players> data = Collections.emptyList();

    public SPlayersTeamsAdapter(Context c, List<Players> players) {
        context = c;
        inflater = LayoutInflater.from(context);
        data = players;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.slayoutplayers, parent,false);
        return new SPlayersTeamsAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        SPlayersTeamsAdapter.MyHolder myHolder= (SPlayersTeamsAdapter.MyHolder) holder;
        final Players current = data.get(position);
        myHolder.textName.setText(current.getName() + " " + current.getFName());
        myHolder.textNumber.setText((current.getNumber() + ""));
        myHolder.textGP.setText((current.getGP() + ""));
        myHolder.textG.setText((current.getGoals() + ""));
        myHolder.textYC.setText((current.getYC() + ""));
        myHolder.textRC.setText((current.getRC() + ""));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView textName;
        TextView textFName;
        TextView textNumber;
        TextView textGP;
        TextView textG;
        TextView textYC;
        TextView textRC;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.playerName);
            textNumber= (TextView) itemView.findViewById(R.id.playerNumber);
            textGP= (TextView) itemView.findViewById(R.id.playerGP);
            textG= (TextView) itemView.findViewById(R.id.playerGoals);
            textYC = (TextView) itemView.findViewById(R.id.playerYC);
            textRC= (TextView) itemView.findViewById(R.id.playerRC);
        }

    }
}
