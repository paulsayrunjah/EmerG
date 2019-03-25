package com.groupbsse.ourapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupbsse.ourapp.adapters.TrackMeAdapter;
import com.groupbsse.ourapp.classes.TrackMe;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrackedUsersFragment extends Fragment {


    public TrackedUsersFragment() {
        // Required empty public constructor
    }

    TextView tv_empty;
    RecyclerView recyclerView;
    TrackMeAdapter trackMeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tracked_users, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tv_empty = (TextView)rootView.findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onResume() {
        loadTracks();
        super.onResume();
    }

    private void loadTracks(){
        List<TrackMe> trackMes = TrackMe.listAll(TrackMe.class);
        if(trackMes.size() > 0){
            trackMeAdapter = new TrackMeAdapter(getActivity(), trackMes);
            recyclerView.setAdapter(trackMeAdapter);
            tv_empty.setVisibility(View.GONE);
        }else{
            tv_empty.setVisibility(View.VISIBLE);
        }

    }

}
