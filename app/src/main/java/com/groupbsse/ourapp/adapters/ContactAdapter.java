package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.Contact;
import com.groupbsse.ourapp.classes.ServiceList;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ContactAdapter
        extends RecyclerView.Adapter
        <ContactAdapter.ListItemViewHolder> {

    ArrayList<Contact> data= new ArrayList<>();
    private ArrayList<Contact> arraylist;
    private LayoutInflater mInflater;
    int lastPosition=0;

    Context mcontext;
    private SparseBooleanArray selectedItems;
    public String filterContactName;


    public ContactAdapter(Context context, ArrayList<Contact> modelData) {
        this.mcontext= context;
        this. data = modelData;
        this.arraylist = new ArrayList<Contact>();
        this.arraylist.addAll(data);
        selectedItems = new SparseBooleanArray();
        this.mInflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.filterContactName="";

    }
    public void addContacts(List<Contact> contacts){
        try {
            this.data.clear();
            this.arraylist.clear();

            this.data.addAll(contacts);
            this.arraylist.addAll(contacts);
            this.filter(this.filterContactName);
        } catch (Exception e) {
            e.printStackTrace();
            this.arraylist.addAll(data);
            notifyDataSetChanged();
        }

    }
    private void animate(RecyclerView.ViewHolder viewHolder, final int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position >= lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mcontext, R.anim.push_left_in);
            animation.setDuration(500);
            viewHolder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }


    public Contact getItem(int position) {
        return data.get(position);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.
                inflate(R.layout.contact_item, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        if(viewHolder instanceof ListItemViewHolder) {

            Contact contact = data.get(position);
            viewHolder.name.setText(contact.getName());
            viewHolder.number.setText(contact.getNumber());
            viewHolder.chk_contact.setChecked(selectedItems.get(position, false));

            if(selectedItems.get(position, false)== true){
                viewHolder.itemView.setBackgroundResource(R.color.colorSelected);
            }else{
                viewHolder.itemView.setBackgroundResource(0);
            }

        }

        animate(viewHolder, position);
    }

    public void updateSelected(){

        if(ServiceList.selected_positions.size() != 0){
            selectedItems.clear();
            int i = 0;
            while(i < ServiceList.selected_positions.size()){
                selectedItems.put(ServiceList.my_positions.get(i),true);
                i++;
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            ServiceList.selected_positions.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
            ServiceList.selected_positions.put(pos, true);
            ServiceList.my_positions.add(pos);
        }
        notifyItemChanged(pos);
    }

    public void selectAll(){
        selectedItems.clear();
        int i=0;
        while (i<data.size()){
            selectedItems.put(i, true);
            i++;
        }
        notifyDataSetChanged();
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView name,number;
        CheckBox chk_contact;
        public ListItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            number = (TextView)itemView.findViewById(R.id.number);
            chk_contact = (CheckBox) itemView.findViewById(R.id.chk_contact);

        }
    }


    public void filter(String paramString)
    {
        String str = paramString.toLowerCase(Locale.getDefault());
        this.data.clear();
        if (str.length() == 0){
            this.filterContactName = "";
            this.data.addAll(arraylist);
            Log.d("search", arraylist.size()+"");
        }else{
            this.filterContactName = paramString;
            Iterator<Contact> localIterator = this.arraylist.iterator();
            while (localIterator.hasNext())
            {
                Contact contact = (Contact) localIterator.next();
                if (contact.name.toLowerCase(Locale.getDefault()).contains(str)
                        || contact.number.toLowerCase(Locale.getDefault()).contains(str))
                    this.data.add(contact);
            }
        }

        notifyDataSetChanged();
    }
}
