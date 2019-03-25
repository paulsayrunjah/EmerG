package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class OtherContactAdapter extends RecyclerView.Adapter<OtherContactAdapter.ViewHolder> {

    List<Contacts> arrayList = new ArrayList<>();
    Context context;

    public OtherContactAdapter(Context context, List<Contacts> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       Contacts contacts = arrayList.get(position);
        holder.chk_contact.setVisibility(View.GONE);
        holder.name.setText(contacts.getName());
        holder.number.setText(contacts.getNumber());

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

        TextView name,number;
        CheckBox chk_contact;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            number = (TextView)itemView.findViewById(R.id.number);
            chk_contact = (CheckBox) itemView.findViewById(R.id.chk_contact);

        }

    }
}
