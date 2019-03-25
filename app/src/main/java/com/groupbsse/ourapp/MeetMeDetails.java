package com.groupbsse.ourapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetMeDetails extends Fragment {

    Bundle extras;
   private String image,desc,location;
   private TextView tvdesc,tvlocation;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_meet_me_details, container, false);

        tvdesc = (TextView)rootView.findViewById(R.id.tvdesc);
        tvlocation = (TextView)rootView.findViewById(R.id.tvlocation);
        imageView = (ImageView)rootView.findViewById(R.id.imageView);
        extras = getArguments();

        if(extras != null){
            image = extras.getString("image");
            desc = extras.getString("desc");
            location = extras.getString("location");

            Glide.with(getActivity())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
            tvdesc.setText(desc);
            tvlocation.setText(location);

        }

        return rootView;
    }

}
