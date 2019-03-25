package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.NotificationClass;
import com.groupbsse.ourapp.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<NotificationClass> arrayList = new ArrayList<>();
    Context context;

    public NotificationAdapter(Context context, List<NotificationClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_notification, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationClass notificationClass = arrayList.get(position);
        holder.desc.setText(notificationClass.getDesc());
        holder.time.setText(new CommonUtils(context).getTimeago(notificationClass.getTime()));
        if(notificationClass.getType().equals("addfriend")){
            holder.linearLayout.setVisibility(View.GONE);
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

        TextView desc,time;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.desc);
            time = (TextView) itemView.findViewById(R.id.time);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.layout_time);
        }

    }
}
