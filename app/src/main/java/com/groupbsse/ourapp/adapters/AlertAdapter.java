package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.AlertMessage;

import java.util.ArrayList;

/**
 * Created by Sayrunjah on 02/03/2017.
 */
public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {

    ArrayList<AlertMessage> arrayList = new ArrayList<>();
    Context context;
    int type;

    public AlertAdapter(Context context, ArrayList<AlertMessage> arrayList, int type) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_my_alert, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlertMessage alert_message = arrayList.get(position);

        if(type == 0){
            if(alert_message.isSeen() == false){
                holder.seen.setVisibility(View.VISIBLE);
            }
            holder.description.setText("Alert Message: "+alert_message.getDescription());
            holder.place.setText(alert_message.getLocation_name());
        }else{
            holder.description.setText("Alert Message: "+alert_message.getDescription());
            holder.place.setText(alert_message.getLocation_name());
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

        TextView description,place,seen;
        public ViewHolder(View itemView) {
            super(itemView);
            description = (TextView)itemView.findViewById(R.id.description);
            place = (TextView)itemView.findViewById(R.id.place);
            seen = (TextView)itemView.findViewById(R.id.seen);
        }

    }
}
