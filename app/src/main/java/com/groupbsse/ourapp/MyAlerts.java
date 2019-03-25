package com.groupbsse.ourapp;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.groupbsse.ourapp.adapters.AlertAdapter;
import com.groupbsse.ourapp.classes.AlertMessage;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
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
public class MyAlerts extends Fragment {


    RecyclerView recyclerView;
    ArrayList<AlertMessage> arrayList;
    AlertAdapter alertAdapter;
    RecyclerView.LayoutManager layoutManager;

    Connection connection;
    ProgressDialog progressDialog;
    TextView tv_empty;
    ProgressBar progressBar;
    List<AlertMessage> alertsList;

    Bundle extras;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_alerts, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        tv_empty = (TextView)rootView.findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        connection = new Connection();
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);

        extras = getArguments();
        if(extras != null){
            if(extras.getBoolean("notification")== true){
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        AlertMessage alert_message = alertsList.get(position);
                        Intent details = new Intent(getActivity(),AlertDetails.class);
                        details.putExtra("location",alert_message.getLocation_name());
                        details.putExtra("desc",alert_message.getDescription());
                        details.putExtra("lat",alert_message.getLatitude());
                        details.putExtra("lng",alert_message.getLongitude());
                        details.putExtra("username",alert_message.getSender_name());
                        startActivity(details);

                        AlertMessage alm = AlertMessage.findById(AlertMessage.class,alert_message.getId());
                        alm.seen = true;
                        alm.save();
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
                    AlertMessage alert_message = arrayList.get(position);
                    Intent details = new Intent(getActivity(),AlertDetails.class);
                    details.putExtra("location",alert_message.getLocation_name());
                    details.putExtra("desc",alert_message.getDescription());
                    details.putExtra("lat",alert_message.getLatitude());
                    details.putExtra("lng",alert_message.getLongitude());
                    details.putExtra("username",alert_message.getSender_name());
                    startActivity(details);
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
                progressBar.setVisibility(View.GONE);

                alertsList = AlertMessage.listAll(AlertMessage.class);
                if(alertsList.size() > 0){
                    alertAdapter = new AlertAdapter(getActivity(), (ArrayList<AlertMessage>) alertsList,0);
                    recyclerView.setAdapter(alertAdapter);
                    tv_empty.setVisibility(View.GONE);
                    for(AlertMessage al : alertsList){
                        Log.d("AlertMessage :","Desc :"+al.getDescription()+" Sender:"+al.getSender_name());
                    }

                }else{
                    tv_empty.setVisibility(View.VISIBLE);
                    tv_empty.setText("No Alerts");
                }
            }

        }else{
            if(arrayList ==  null)
                arrayList = new ArrayList<>();
            else if(arrayList.size() > 0)
                arrayList.clear();

            if(new Myprefs(getActivity()).getUserData().get("userid") != null){
                getMyAlerts(new Myprefs(getActivity()).getUserData().get("userid"));
            }else{
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("Not registered");
            }


            ItemTouchHelper.Callback callback = new RecyclerSwipeListener(alertAdapter, new RecyclerSwipeListener.SwipeListener() {
                @Override
                public void onSiwpe(int position) {
                    Toast.makeText(getActivity(),arrayList.get(position).getLocation_name(),Toast.LENGTH_SHORT).show();
                }
            });
            ItemTouchHelper helper = new ItemTouchHelper(callback);
            helper.attachToRecyclerView(recyclerView);


        }

        super.onResume();
    }

    public void getMyAlerts(String userid){
        if(arrayList ==  null)
            arrayList = new ArrayList<>();
        else if(arrayList.size() > 0)
            arrayList.clear();

        /*if(AlertsFragment.viewPager.getCurrentItem() == 1){
            progressDialog = ProgressDialog.show(MyAlerts.this.getActivity(),null,"please wait ...");
        }*/

        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        connection.makeRequest(LinkUrls.getMyAlerts, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("response",response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       /* if(progressDialog != null)
                            progressDialog.dismiss();*/

                        progressBar.setVisibility(View.GONE);
                        String message_id,userid,desc,location,name,time;
                        double lat,lng;
                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                String success = jsonArray.getJSONObject(jsonArray.length()-1).getString("success");

                                if(success.equals("1")){
                                    for(int i =0 ;i <jsonArray.length()-1;i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        message_id = jsonObject.getString("message_id");
                                        userid = jsonObject.getString("id");
                                        desc = jsonObject.getString("desc");
                                        location = jsonObject.getString("location");
                                        lat = jsonObject.getDouble("lat");
                                        lng = jsonObject.getDouble("lng");
                                        name = jsonObject.getString("name");
                                        time = jsonObject.getString("time");
                                        arrayList.add(new AlertMessage(message_id,userid,desc,location,lat,lng,name,time));
                                    }
                                    alertAdapter = new AlertAdapter(getActivity(),arrayList,1);
                                    recyclerView.setAdapter(alertAdapter);
                                    tv_empty.setVisibility(View.GONE);
                                }else{
                                   tv_empty.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                           progressBar.setVisibility(View.GONE);
                            tv_empty.setVisibility(View.VISIBLE);
                            tv_empty.setText("Check internet connection");
                        }
                    }
                });

            }
        });
    }

}
