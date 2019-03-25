package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.CustomMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class CustomMessageAdapter extends RecyclerView.Adapter<CustomMessageAdapter.ViewHolder> {

    List<CustomMessage> arrayList = new ArrayList<>();
    Context context;

    public CustomMessageAdapter(Context context, List<CustomMessage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_custom_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomMessage message = arrayList.get(position);
        holder.message.setText(message.getMessage());

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

        TextView message;

        public ViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
        }

    }
}