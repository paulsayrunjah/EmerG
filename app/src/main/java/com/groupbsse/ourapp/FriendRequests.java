package com.groupbsse.ourapp;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.groupbsse.ourapp.adapters.FriendListAdapter;
import com.groupbsse.ourapp.classes.FriendClass;
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
public class FriendRequests extends Fragment {


    RecyclerView recyclerView2;
    FriendListAdapter friendListAdapter2;
    List<FriendClass> arrayList2;
    ProgressBar progressBar;
    TextView tv_empty;

    Bundle extras;
    List<FriendClass> friendClassList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friend_requests, container, false);
        recyclerView2 = (RecyclerView)rootView.findViewById(R.id.friends);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        tv_empty = (TextView)rootView.findViewById(R.id.tv_empty);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView2, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final FriendClass friendClass = arrayList2.get(position);
                new CommonUtils(FriendRequests.this.getActivity()).ConfirmationDialog("Accept request from "+ friendClass.getName(), null, new CommonUtils.MyAction() {
                    @Override
                    public void actionPerfomed() {
                        confirmRequest(friendClass.getFid());
                    }

                    @Override
                    public void actionCancel() {

                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return rootView;
    }

    @Override
    public void onResume() {
        if(arrayList2 == null){
            arrayList2 = new ArrayList<>();
        }else if(arrayList2.size() > 0){
            arrayList2.clear();
        }

        loadList(new Myprefs(getActivity()).getUserData().get("userid"));
        super.onResume();
    }

    public void loadList(String userid){
        Connection connection = new Connection();
        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        map.put("command","requests");
        connection.makeRequest(LinkUrls.getFriendsList, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("Response",response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        progressBar.setVisibility(View.GONE);

                        String status,name,number,fid,userid,image;
                        String success;
                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(jsonObject.getString("success").equals("1")){
                                        name = jsonObject.getString("name");
                                        number = jsonObject.getString("number");
                                        fid = jsonObject.getString("fid");
                                        userid = jsonObject.getString("userid");
                                        status = jsonObject.getString("status");
                                        image = "0";
                                        arrayList2.add(new FriendClass(userid,name,number,image,fid));
                                    }else{

                                    }
                                }

                                if(arrayList2.size() > 0){
                                    friendListAdapter2 = new FriendListAdapter(getActivity(),arrayList2);
                                    recyclerView2.setAdapter(friendListAdapter2);
                                    tv_empty.setVisibility(View.GONE);
                                }else{
                                    tv_empty.setVisibility(View.VISIBLE);
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{

                        }

                    }
                });

            }
        });



    }

    public void confirmRequest(String id){
        Connection connection = new Connection();
        HashMap<String ,String> map = new HashMap<>();
        map.put("friendslist_id",id);
        map.put("username",new Myprefs(getActivity()).getUserData().get("username"));
        map.put("command","prove");
        connection.makeRequest(LinkUrls.searchUser, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("Response",response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i =0 ;i <jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(jsonObject.getString("success").equals("1")){

                                        new CommonUtils(FriendRequests.this.getActivity()).MessageDialog("Successfull", null, new CommonUtils.MyAction() {
                                            @Override
                                            public void actionPerfomed() {

                                            }

                                            @Override
                                            public void actionCancel() {

                                            }
                                        });
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{

                        }
                    }
                });

            }
        });
    }

}
