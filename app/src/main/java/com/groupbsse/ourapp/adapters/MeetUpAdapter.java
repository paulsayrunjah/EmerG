package com.groupbsse.ourapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.MeetUp;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class MeetUpAdapter extends RecyclerView.Adapter<MeetUpAdapter.ViewHolder> {

    ArrayList<MeetUp> arrayList = new ArrayList<>();
    Context context;
    int type;

    public MeetUpAdapter(Context context, ArrayList<MeetUp> arrayList, int type) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_meet_up, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MeetUp meet_up = arrayList.get(position);

        if(type == 0){
            if(meet_up.isSeen() == false){
                holder.seen.setVisibility(View.VISIBLE);
            }
            holder.name.setText(meet_up.getSender_name());
            holder.description.setText("Description :"+meet_up.getDescription());
            holder.place.setText(meet_up.getLocation_name());
            if (android.os.Build.VERSION.SDK_INT >= 10) {
                try {
                    Glide.with(context)
                            .load(meet_up.getImage())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception arg0, String arg1,
                                                           Target<GlideDrawable> arg2, boolean arg3) {
                                    //localImageView.setImageResource(R.drawable.ic_empty);
                                    //progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable arg0, String arg1,
                                                               Target<GlideDrawable> arg2, boolean arg3, boolean arg4) {
                                    //progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })//.crossFade()
                            //.centerCrop()
                            .into(holder.imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                Uri localUri = Uri.parse(meet_up.getImage());
                Picasso.with(context).load(localUri).into(holder.imageView, new Callback() {
                    @Override
                    public void onError() {
                        //imageView.setImageResource(R.drawable.ic_empty);
                        //progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess() {
                        //progressBar.setVisibility(View.GONE);
                    }
                });

            }
        }else{
            holder.name.setText(meet_up.getSender_name());
            holder.description.setText("Description :"+meet_up.getDescription());
            holder.place.setText(meet_up.getLocation_name());
            if (android.os.Build.VERSION.SDK_INT >= 10) {
                try {
                    Glide.with(context)
                            .load(meet_up.getImage())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception arg0, String arg1,
                                                           Target<GlideDrawable> arg2, boolean arg3) {
                                    //localImageView.setImageResource(R.drawable.ic_empty);
                                    //progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable arg0, String arg1,
                                                               Target<GlideDrawable> arg2, boolean arg3, boolean arg4) {
                                    //progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })//.crossFade()
                            //.centerCrop()
                            .into(holder.imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                Uri localUri = Uri.parse(meet_up.getImage());
                Picasso.with(context).load(localUri).into(holder.imageView, new Callback() {
                    @Override
                    public void onError() {
                        //imageView.setImageResource(R.drawable.ic_empty);
                        //progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess() {
                        //progressBar.setVisibility(View.GONE);
                    }
                });

            }
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

        TextView name,description,place,seen;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            place = (TextView) itemView.findViewById(R.id.place);
            seen = (TextView) itemView.findViewById(R.id.seen);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
        }

    }
}
