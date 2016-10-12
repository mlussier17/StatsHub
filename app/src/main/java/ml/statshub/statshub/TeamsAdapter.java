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

public class TeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    List<Teams> data = Collections.emptyList();

    public TeamsAdapter(Context c, List<Teams> teams) {
        context = c;
        inflater = LayoutInflater.from(context);
        data = teams;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.layoutteams, parent,false);
        TeamsAdapter.MyHolder holder=new TeamsAdapter.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        TeamsAdapter.MyHolder myHolder= (TeamsAdapter.MyHolder) holder;
        final Teams current = data.get(position);
        myHolder.textName.setText(current.getName());
        myHolder.textSize.setText(current.getWins() + "");
//        myHolder.textName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, TeamsActivity.class);
//                Bundle b = new Bundle();
//                b.putInt("id",current.getId());
//                b.putString("name",current.getName());
//                i.putExtras(b);
//                //context.startActivity(i);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView textName;
        TextView textSize;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.teamName);
            textSize = (TextView) itemView.findViewById(R.id.teamWins);
        }

    }
}
