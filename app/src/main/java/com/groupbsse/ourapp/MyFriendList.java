package com.groupbsse.ourapp;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.groupbsse.ourapp.adapters.FriendListAdapter;
import com.groupbsse.ourapp.classes.FriendClass;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.Myprefs;
import com.groupbsse.ourapp.util.RecyclerSwipeListener;
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
public class MyFriendList extends Fragment {

    RecyclerView recyclerView2;
    FriendListAdapter friendListAdapter2;
    List<FriendClass> arrayList2;
    ProgressBar progressBar;
    TextView tv_empty;

    Bundle extras;
    List<FriendClass> friendClassList;
    Long localdbId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_friend_list, container, false);
        recyclerView2 = (RecyclerView)rootView.findViewById(R.id.friends);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        tv_empty = (TextView)rootView.findViewById(R.id.tv_empty);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onResume() {

      /*  extras = getArguments();

        if(extras != null){
            if(extras.getBoolean("notification")== true){
                progressBar.setVisibility(View.GONE);
                friendClassList = FriendClass.listAll(FriendClass.class);
                if(friendClassList.size() > 0){
                    friendListAdapter2 = new FriendListAdapter(getActivity(),friendClassList);
                    recyclerView2.setAdapter(friendListAdapter2);
                    tv_empty.setVisibility(View.GONE);
                    for(FriendClass al : friendClassList){
                        Log.d("FrRquet :","Sender :"+al.getUserid()+" Desc:"+al.getDesc());
                    }
                }else{
                    tv_empty.setText("No friend requests");
                    tv_empty.setVisibility(View.VISIBLE);
                }


            }
        }else {

        }*/
        if(arrayList2 == null){
            arrayList2 = new ArrayList<>();
        }else if(arrayList2.size() > 0){
            arrayList2.clear();
        }

        ItemTouchHelper.Callback callback = new RecyclerSwipeListener(friendListAdapter2, new RecyclerSwipeListener.SwipeListener() {
            @Override
            public void onSiwpe(final int position) {
                final FriendClass friendClass = arrayList2.get(position);
                friendListAdapter2.notifyDataSetChanged();
                new CommonUtils(MyFriendList.this.getActivity()).ConfirmationDialog("Removing " + friendClass.getName() + " from friend list", null, new CommonUtils.MyAction() {
                    @Override
                    public void actionPerfomed() {
                        remove_friend(new Myprefs(getActivity()).getUserData().get("userid"), friendClass.getFid(),position);
                        //Toast.makeText(getActivity(), friendClass.getName() +" "+friendClass.getFid(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void actionCancel() {

                    }
                });
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView2);

        loadList(new Myprefs(getActivity()).getUserData().get("userid"));
        super.onResume();
    }

    public void loadList(String userid){
        Connection connection = new Connection();
        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        map.put("command","friends");
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
                                        arrayList2.add(new FriendClass(userid,name,number,image,userid));
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

    private void remove_friend(String userid, String fid, final int position){
        Connection connection = new Connection();
        HashMap<String, String> map = new HashMap<>();
        map.put("userid", userid);
        map.put("fid", fid);
        map.put("command", "unfriend");

        connection.makeRequest(LinkUrls.searchUser, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("response", response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(jsonObject.getString("success").equals("1")){
                                        friendListAdapter2.remove(position);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            new CommonUtils(getActivity()).MessageDialog("failed", null, new CommonUtils.MyAction() {
                                @Override
                                public void actionPerfomed() {

                                }

                                @Override
                                public void actionCancel() {

                                }
                            });
                        }

                    }
                });
            }
        });

    }


}
