package com.groupbsse.ourapp;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.groupbsse.ourapp.adapters.MyFriendsAdapter;
import com.groupbsse.ourapp.classes.ServiceList;
import com.groupbsse.ourapp.classes.TrackMe;
import com.groupbsse.ourapp.classes.Users;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.Myprefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrackMeFragment extends Fragment implements ReciversDialog.OnComplete{

    TextView tvShow;
    Button trackme,stop, btn_recivers;

    AlertDialog alertDialog;
    MyFriendsAdapter myFriendsAdapter;
    ArrayList<Users> selected;
    ArrayList<Users> arrayList;
    RecyclerView recyclerView;
    Connection connection;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_track_me, container, false);
        trackme = (Button)rootView.findViewById(R.id.btn_trackme);
        btn_recivers = (Button)rootView.findViewById(R.id.btn_recivers);
        stop = (Button)rootView.findViewById(R.id.stop);
        tvShow = (TextView)rootView.findViewById(R.id.tvShow);
        tvShow.setVisibility(View.GONE);
        btn_recivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ReciversDialog reciversDialog = new ReciversDialog();
                reciversDialog.show(getFragmentManager(), "dialog");
                reciversDialog.setCompleteListener(TrackMeFragment.this)*/;

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TrackMeFragment.this.getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.reciver_dialog, null);
                dialogBuilder.setView(dialogView);

                connection = new Connection();
                recyclerView = (RecyclerView)dialogView.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                progressBar = (ProgressBar)dialogView.findViewById(R.id.progress);
                TextView empty = (TextView)dialogView.findViewById(R.id.empty);
                final EditText search = (EditText) dialogView.findViewById(R.id.search);
                Button done = (Button)dialogView.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected = new ArrayList<Users>();
                        ArrayList<Users> stList = ((MyFriendsAdapter) myFriendsAdapter).getStudentist();
                        for(Users us : stList){
                            if(us.isSelected() == true){
                                selected.add(new Users(us.getUserid(),us.getImage(),us.getUsername(),us.getPhone()));
                                //Toast.makeText(ReciversDialog.this.getActivity(),us.getUsername()+" "+us.getPhone(),Toast.LENGTH_SHORT).show();
                            }

                        }

                        ServiceList.usersArrayList.clear();
                        ServiceList.usersArrayList.addAll(selected);
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject1 = new JSONObject();
                        JSONObject jsonObject = null;
                        try {
                            for(Users us : ServiceList.usersArrayList){
                                jsonObject = new JSONObject();
                                jsonObject.put("Id",us.getUserid());
                                jsonArray.put(jsonObject);
                            }
                            jsonObject1.put("Recivers",jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Myprefs(getActivity()).storeTrackMeUsers(String.valueOf(jsonObject1));
                        alertDialog.dismiss();

                    }
                });
                alertDialog = dialogBuilder.create();
                alertDialog.show();
                getFriendList(new Myprefs(getActivity()).getUserData().get("userid"));
            }
        });

        trackme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myService = new Intent(getActivity(),TrackMeService.class);
                getActivity().startService(myService);
                tvShow.setVisibility(View.VISIBLE);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(new Intent(getActivity().getBaseContext(), TrackMeService.class));
                tvShow.setVisibility(View.GONE);
                new Myprefs(getActivity()).storeTrackMeUsers(null);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        if(isMyServiceRunning(TrackMeService.class)){
            tvShow.setVisibility(View.VISIBLE);
        }else{
            tvShow.setVisibility(View.GONE);
        }
        super.onResume();
    }



    @Override
    public void userList(ArrayList<Users> list) {
        ServiceList.usersArrayList.clear();
        ServiceList.usersArrayList.addAll(list);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject = null;
        try {
            for(Users us : ServiceList.usersArrayList){
                jsonObject = new JSONObject();
                jsonObject.put("Id",us.getUserid());
                jsonArray.put(jsonObject);
            }
            jsonObject1.put("Recivers",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Myprefs(getActivity()).storeTrackMeUsers(String.valueOf(jsonObject1));
    }

    public void getFriendList(String userid){
        if(arrayList == null)
            arrayList = new ArrayList<>();
        else if(arrayList.size() > 0)
            arrayList.clear();

        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        map.put("command","friends");
        connection.makeRequest(LinkUrls.getFriendsList, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        String name,phone,userid;
                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(jsonObject.getString("success").equals("1")){
                                        name = jsonObject.getString("name");
                                        phone = jsonObject.getString("number");
                                        userid = jsonObject.getString("userid");
                                        arrayList.add(new Users(userid,"",name,phone));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(arrayList.size() > 0){
                                myFriendsAdapter = new MyFriendsAdapter(getActivity(),arrayList);
                                recyclerView.setAdapter(myFriendsAdapter);
                                //empty.setVisibility(View.GONE);
                            }else{
                                //empty.setVisibility(View.VISIBLE);
                            }


                        }

                    }
                });
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
