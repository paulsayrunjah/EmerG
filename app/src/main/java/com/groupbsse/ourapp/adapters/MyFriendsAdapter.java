package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.Users;

import java.util.ArrayList;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class MyFriendsAdapter  extends RecyclerView.Adapter<MyFriendsAdapter.ViewHolder> {

    ArrayList<Users> arrayList = new ArrayList<>();
    Context context;

    public MyFriendsAdapter(Context context, ArrayList<Users> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_recivers, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            final Users users = arrayList.get(position);
            holder.name.setText(users.getUsername());
            holder.phone.setText(users.getPhone());

            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(users.isSelected());
            holder.checkBox.setTag(users);


           holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Users user = (Users) cb.getTag();

                user.setSelected(cb.isChecked());
                users.setSelected(cb.isChecked());

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void remove(int position) {
        arrayList.remove(position);
        notifyDataSetChanged();
    }

    public ArrayList<Users> getStudentist() {
        return arrayList;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,phone;
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            checkBox = (CheckBox)itemView.findViewById(R.id.chkSelected);
        }

    }
}
