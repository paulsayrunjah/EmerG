package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.TrackMe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class TrackMeAdapter extends RecyclerView.Adapter<TrackMeAdapter.ViewHolder> {

    List<TrackMe> arrayList = new ArrayList<>();
    Context context;

    public TrackMeAdapter(Context context, List<TrackMe> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_track_me, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrackMe trackMe = arrayList.get(position);
        holder.sender.setText(trackMe.getSender());
        holder.place.setText(trackMe.getLocation());
        holder.cordinates.setText(trackMe.getLat()+", "+trackMe.getLng());
        holder.time.setText(trackMe.getTime());
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

        TextView sender, place, cordinates, time;
        public ViewHolder(View itemView) {
            super(itemView);
            sender = (TextView)itemView.findViewById(R.id.sender);
            place = (TextView)itemView.findViewById(R.id.place);
            cordinates = (TextView)itemView.findViewById(R.id.cordinates);
            time = (TextView)itemView.findViewById(R.id.time);
        }

    }
}
