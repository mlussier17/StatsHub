package ml.statshub.statshub.Hockey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import ml.statshub.statshub.Class.Leagues;
import ml.statshub.statshub.R;

public class LeaguesLeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Leagues> data;

    LeaguesLeaderAdapter(Context c, List<Leagues> leagues){
        context = c;
        inflater = LayoutInflater.from(context);
        data = leagues;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.layoutleagues, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        final Leagues current = data.get(position);
        myHolder.textName.setText(current.getName());
        myHolder.textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LeadersActivity.class);
                Bundle b = new Bundle();
                b.putInt("id",current.getId());
                b.putString("name",current.getName());
                i.putExtras(b);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {return data.size();}

    class MyHolder extends RecyclerView.ViewHolder{
        TextView textName;
        TextView textSize;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView)itemView.findViewById(R.id.textName);
            textSize = (TextView)itemView.findViewById(R.id.textTeams);
        }
    }
}
