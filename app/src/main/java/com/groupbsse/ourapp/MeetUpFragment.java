package com.groupbsse.ourapp;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.groupbsse.ourapp.adapters.MeetUpAdapter;
import com.groupbsse.ourapp.classes.MeetUp;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.Myprefs;
import com.groupbsse.ourapp.util.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetUpFragment extends Fragment {


    public MeetUpFragment() {
        // Required empty public constructor
    }

    Connection connection;
    CommonUtils commonUtils;

    FloatingActionButton fab;

    ArrayList<MeetUp> arrayList;
    RecyclerView recyclerView;
    MeetUpAdapter meetUpAdapter;
    ProgressBar progressBar;
    TextView empty;

    Bundle extras;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_meet_up, container, false);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        empty = (TextView)rootView.findViewById(R.id.empty);
        extras = getArguments();
        connection = new Connection();
        commonUtils = new CommonUtils(MeetUpFragment.this.getActivity());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getActivity();
                sendMeetPointFragment fragment = new sendMeetPointFragment();
                FragmentManager fm= mainActivity.getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.content_frame, fragment, "meetup")
                        .addToBackStack("map").commit();
            }
        });

        if(extras != null){
            fab.setVisibility(View.GONE);
        }


        return rootView;
    }

    @Override
    public void onResume() {


        if(extras != null){
            if(extras.getBoolean("notification")== true){
                fab.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                List<MeetUp> friendClassList = MeetUp.listAll(MeetUp.class);
                if(friendClassList.size() > 0){
                    meetUpAdapter = new MeetUpAdapter(getActivity(), (ArrayList<MeetUp>) friendClassList,0);
                    recyclerView.setAdapter(meetUpAdapter);
                    for(MeetUp al : friendClassList){
                        Log.d("Meet :","Sender :"+al.getSender_name()+" Desc:"+al.getDescription());
                    }
                }else{
                    empty.setVisibility(View.VISIBLE);
                    empty.setText("No meet up notifications");
                }
            }

        }else{
            if(arrayList == null)
                arrayList = new ArrayList<>();
            else if(arrayList.size() > 0)
                arrayList.clear();

            getList(new Myprefs(getActivity()).getUserData().get("userid"));

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Bundle bundle = new Bundle();
                    MeetUp meetUp = arrayList.get(position);
                    bundle.putString("image",meetUp.getImage());
                    bundle.putString("desc", meetUp.getDescription());
                    bundle.putString("location",meetUp.getLocation_name());

                    MeetMeDetails meetMeDetails = new MeetMeDetails();
                    meetMeDetails.setArguments(bundle);

                    MainActivity act= (MainActivity)getActivity();
                    FragmentManager fm= act.getSupportFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.content_frame, meetMeDetails, "Meet up details").commit();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }
        super.onResume();
    }

    public void getList(String userid){
        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        map.put("link",LinkUrls.address);
        connection.makeRequest(LinkUrls.getMeetUpList, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("response",response);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                       String meet_up_id = "",id = "",desc = "",name ="", image = "",location = "";
                        double lat,lng;
                     if(!response.equals("error")){
                         try {
                             JSONArray jsonArray = new JSONArray(response);
                             String success = jsonArray.getJSONObject(jsonArray.length()-1).getString("success");
                             if(success.equals("1")){
                                 for(int i = 0; i < jsonArray.length(); i++){
                                     JSONObject jsonObject = jsonArray.getJSONObject(i);
                                         meet_up_id = jsonObject.getString("meet_up_id");
                                         location = jsonObject.getString("location");
                                         desc = jsonObject.getString("desc");
                                         lat = jsonObject.getDouble("lat");
                                         lng = jsonObject.getDouble("lng");
                                         name = jsonObject.getString("name");
                                         image = jsonObject.getString("image");
                                         arrayList.add(new MeetUp(meet_up_id,desc,location,lat,lng,name,image));
                                 }
                                 empty.setVisibility(View.GONE);
                             }else{
                                 empty.setVisibility(View.VISIBLE);
                             }

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                         meetUpAdapter = new MeetUpAdapter(getActivity(),arrayList,1);
                         recyclerView.setAdapter(meetUpAdapter);
                     }else{
                         empty.setVisibility(View.VISIBLE);
                         empty.setText("No internet cnnection");
                     }
                    }
                });
            }
        });

    }
}
