package com.groupbsse.ourapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.groupbsse.ourapp.adapters.WarnAdapter;
import com.groupbsse.ourapp.classes.ServiceList;
import com.groupbsse.ourapp.classes.Warning;
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
public class WarnFragment extends Fragment {


    public WarnFragment() {
        // Required empty public constructor
    }

    FloatingActionButton fab;

    Connection connection;
    CommonUtils commonUtils;


    ArrayList<Warning> arrayList;
    List<Warning> friendClassList;
    WarnAdapter warnAdapter;
    RecyclerView recyclerView;

    ProgressDialog progressDialog;
    TextView empty;
    Bundle extras;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_warn, container, false);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        empty = (TextView)rootView.findViewById(R.id.empty);

        connection = new Connection();
        commonUtils = new CommonUtils(WarnFragment.this.getActivity());
        extras = getArguments();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity act= (MainActivity)getActivity();
                SendWarning fragment= new SendWarning();
                FragmentManager fm= act.getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.content_frame, fragment, "MapView").commit();
            }
        });

        if(extras != null){
            fab.setVisibility(View.GONE);
            if(extras.getBoolean("notification")== true){
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Warning warning = friendClassList.get(position);

                        Intent myIntent = new Intent(getActivity(),WarnDetails.class);
                        myIntent.putExtra("warningid",warning.getWarning_id());
                        myIntent.putExtra("localdbid",warning.getId());
                        startActivity(myIntent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }
        }else{
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Warning warning = arrayList.get(position);

                    Intent myIntent = new Intent(getActivity(),WarnDetails.class);
                    myIntent.putExtra("desc",warning.getDescription());
                    myIntent.putExtra("audio",warning.getAudio());
                    myIntent.putExtra("image",warning.getImage());
                    myIntent.putExtra("position",position);
                    startActivity(myIntent);
                    //Toast.makeText(getActivity(), warning.getAudio() +" "+warning.getImage(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


        }

        return rootView;
    }

    @Override
    public void onResume() {



        if(extras != null){
            if(extras.getBoolean("notification")== true){
                fab.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                friendClassList = Warning.listAll(Warning.class);
                if(friendClassList.size() > 0){
                    warnAdapter = new WarnAdapter(getActivity(), (ArrayList<Warning>) friendClassList, 0);
                    recyclerView.setAdapter(warnAdapter);
                    for(Warning al : friendClassList){
                        Log.d("Warn :","Sender :"+al.getWarning_id()+" Desc:"+al.getDescription());
                    }

                }else{
                    empty.setVisibility(View.VISIBLE);
                    empty.setText("No warning notifications");
                }
            }

        }else{
            if(arrayList == null)
                arrayList = new ArrayList<>();
            else if(arrayList.size() > 0)
                arrayList.clear();

            getWarnigList();
        }


        super.onResume();
    }

    public void getWarnigList(){
        progressDialog = ProgressDialog.show(WarnFragment.this.getActivity(),null,"Please wait....");
        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",new Myprefs(getActivity()).getUserData().get("userid"));
        map.put("username", new Myprefs(getActivity()).getUserData().get("username"));
        map.put("link",LinkUrls.address);
        connection.makeRequest(LinkUrls.getWarningList, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("response",response);
                ServiceList.cords.clear();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null)
                            progressDialog.dismiss();

                        String id = "",desc = "",image = "",audio = "",time = "";
                        ArrayList<String> recivers = null;
                        ArrayList<LatLng> cords = null;
                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                String success = jsonArray.getJSONObject(jsonArray.length()-1).getString("success");
                                if(success.equals("1")){
                                    for(int i =0; i <jsonArray.length()-1;i++){

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        JSONArray arrayC = jsonObject.getJSONArray("cords");

                                        cords = new ArrayList<LatLng>();
                                        recivers = new ArrayList<String>();

                                        id = jsonObject.getString("id");
                                        desc = jsonObject.getString("desc");
                                        image = jsonObject.getString("image");
                                        audio = jsonObject.getString("audio");
                                        time = jsonObject.getString("sent_at");

                                        for(int c=0; c < arrayC.length();c++){
                                            JSONObject jsonObject1 = arrayC.getJSONObject(c);
                                            Double lat = jsonObject1.getDouble("lat");
                                            Double lng = jsonObject1.getDouble("lng");
                                            cords.add(new LatLng(lat,lng));
                                        }

                                        Log.d("Size List"+String.valueOf(i),String.valueOf(cords.size()));

                                        ServiceList.cords.add(cords);
                                        arrayList.add(new Warning(id,desc,commonUtils.getTimeago(time),image,audio,recivers));
                                        empty.setVisibility(View.GONE);
                                    }
                                }else{
                                    empty.setVisibility(View.VISIBLE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            warnAdapter = new WarnAdapter(getActivity(),arrayList,1);
                            recyclerView.setAdapter(warnAdapter);
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
