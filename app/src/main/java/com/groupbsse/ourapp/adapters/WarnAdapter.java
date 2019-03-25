package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.Warning;
import com.groupbsse.ourapp.util.CommonUtils;

import java.util.ArrayList;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class WarnAdapter extends RecyclerView.Adapter<WarnAdapter.ViewHolder> {

    ArrayList<Warning> arrayList = new ArrayList<>();
    Context context;
    int type;

    public WarnAdapter(Context context, ArrayList<Warning> arrayList, int type) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_warn_, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       Warning warning = arrayList.get(position);
        if(type == 0){
            if(warning.isSeen() == false){
                holder.seen.setVisibility(View.VISIBLE);
            }
            holder.description.setText(warning.getDescription());
            holder.date.setText("Time : "+new CommonUtils(context).getTimeago( warning.getTime()));
            holder.sent_by.setVisibility(View.VISIBLE);
            holder.sent_by.setText("Sent By: "+warning.getSender());
        }else{
            holder.description.setText(warning.getDescription());
            holder.date.setText("Time :"+warning.getTime());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void remove(int position) {
        arrayList.remove(position);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView description,date,sent_by, seen;
        public ViewHolder(View itemView) {
            super(itemView);
            description = (TextView)itemView.findViewById(R.id.desc);
            date = (TextView)itemView.findViewById(R.id.date);
            sent_by = (TextView)itemView.findViewById(R.id.sent_by);
            seen = (TextView)itemView.findViewById(R.id.seen);
        }

    }
}
