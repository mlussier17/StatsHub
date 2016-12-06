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

class PlayersTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater inflater;
    private List<Players> data = Collections.emptyList();

    PlayersTeamsAdapter(Context c, List<Players> players) {
        inflater = LayoutInflater.from(c);
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
        myHolder.textName.setText(current.getName() + " " + current.getFName());
        myHolder.textNumber.setText(current.getNumber() + "");
        myHolder.textGP.setText(current.getGP() + "");
        myHolder.textG.setText(current.getGoals() + "");
        myHolder.textA.setText(current.getAssists() + "");
        myHolder.textPts.setText(current.getPts() + "");
        myHolder.textPPG.setText(current.getPPG() + "");
        myHolder.textPKG.setText(current.getPKG() + "");
        myHolder.textPen.setText(current.getPen() + "");
    }

    @Override
    public int getItemCount() {return data.size();}

    class MyHolder extends RecyclerView.ViewHolder{

        TextView textName;
        TextView textNumber;
        TextView textGP;
        TextView textG;
        TextView textA;
        TextView textPts;
        TextView textPPG;
        TextView textPKG;
        TextView textPen;

        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.playerName);
            textNumber= (TextView) itemView.findViewById(R.id.playerNumber);
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
