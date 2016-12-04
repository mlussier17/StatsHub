package ml.statshub.statshub.Hockey;

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

public class PlayersLeadersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Players> data = Collections.emptyList();

    public PlayersLeadersAdapter(Context c, List<Players> players) {
        context = c;
        inflater = LayoutInflater.from(context);
        data = players;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.layoutplayersleaders, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        final Players current = data.get(position);
        myHolder.textName.setText(current.getName() + " " + current.getFName());
        myHolder.textTeam.setText(current.getTeam());
        myHolder.textNumber.setText(current.getNumber() + "");
        myHolder.textGP.setText(current.getGP() + "");
        myHolder.textG.setText(current.getGoals() + "");
        myHolder.textA.setText(current.getAssists() + "");
        myHolder.textPts.setText(current.getPts() + "");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView textName;
        TextView textNumber;
        TextView textTeam;
        TextView textGP;
        TextView textG;
        TextView textA;
        TextView textPts;

        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.playerName);
            textTeam= (TextView) itemView.findViewById(R.id.playerTeam);
            textNumber= (TextView) itemView.findViewById(R.id.playerNumber);
            textGP= (TextView) itemView.findViewById(R.id.playerGP);
            textG= (TextView) itemView.findViewById(R.id.playerGoals);
            textA= (TextView) itemView.findViewById(R.id.playerAssists);
            textPts= (TextView) itemView.findViewById(R.id.playerPoints);
        }

    }
}
