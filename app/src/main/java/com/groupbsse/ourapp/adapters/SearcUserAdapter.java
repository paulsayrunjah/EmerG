package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.Contact;

import java.util.ArrayList;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class SearcUserAdapter extends RecyclerView.Adapter<SearcUserAdapter.ViewHolder> {

    ArrayList<Contact> arrayList = new ArrayList<>();
    Context context;

    public SearcUserAdapter(Context context, ArrayList<Contact> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_app_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       Contact contact = arrayList.get(position);
        holder.name.setText(contact.getName());
        holder.number.setText(contact.getNumber());
        if(contact.isApp_user() == 1){
            holder.status.setText("Friends");
        }else if(contact.isApp_user() == 0){
            holder.status.setText("Pending Request");
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

        TextView name,number,status;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            number = (TextView)itemView.findViewById(R.id.number);
            status = (TextView)itemView.findViewById(R.id.friend_status);
        }

    }
}