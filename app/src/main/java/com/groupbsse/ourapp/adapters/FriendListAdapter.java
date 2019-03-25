package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.FriendClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    List<FriendClass> arrayList = new ArrayList<>();
    Context context;

    public FriendListAdapter(Context context, List<FriendClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_request_friends,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FriendClass friendClass = arrayList.get(position);
        holder.name.setText(friendClass.getName());
        holder.number.setText(friendClass.getNumber());
        if(!friendClass.getImage().equals("0")){
            Glide.with(context)
                    .load(friendClass.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.imageView);
        }
        /*if(friendClass.getTable_id().equals("1")){
            holder.status.setVisibility(View.GONE);
        }*/



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

        ImageView imageView;
        TextView name,number,status;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.profile_image);
            name = (TextView)itemView.findViewById(R.id.name);
            number = (TextView)itemView.findViewById(R.id.number);
            status = (TextView)itemView.findViewById(R.id.status);
            status.setVisibility(View.GONE);

        }

    }
}
