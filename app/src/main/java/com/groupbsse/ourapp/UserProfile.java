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
import com.groupbsse.ourapp.util.Myprefs;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfile extends Fragment {


    public UserProfile() {
        // Required empty public constructor
    }

    ImageView imageView;
    TextView name,phone;

    Myprefs myprefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        myprefs = new Myprefs(getActivity());
        imageView = (ImageView)rootView.findViewById(R.id.profile_image);
        name = (TextView)rootView.findViewById(R.id.name);
        phone = (TextView)rootView.findViewById(R.id.phone);

        myprefs = new  Myprefs(getActivity());

        name.setText(myprefs.getUserData().get("username"));
        phone.setText(myprefs.getUserData().get("phone"));

       /* if(myprefs.getUserData().get("image").equals("blank")){

            //Toast.makeText(getActivity(),myprefs.getUserData().get("image") + "No image",Toast.LENGTH_SHORT).show();
        }else{
            Glide.with(getActivity())
                    .load(myprefs.getUserData().get("image"))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }*/

        return rootView;
    }

}
