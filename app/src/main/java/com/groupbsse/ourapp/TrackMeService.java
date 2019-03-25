package com.groupbsse.ourapp;

import android.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.groupbsse.ourapp.classes.MyLocation;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.LocationTracker;
import com.groupbsse.ourapp.util.Myprefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TrackMeService extends Service {
    public TrackMeService() {
    }

    private final int TIME_INTERVAL = 2000;
    public Runnable mRunnable = null;
    Handler mHandler;
    Myprefs myprefs;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myprefs = new Myprefs(getApplicationContext());
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {

                if(myprefs.getTrackMeRecivers() != null){
                    sendTrackInfo(myprefs.getTrackMeRecivers());
                }else {
                    Toast.makeText(getApplicationContext(), "Please add at least one receiver", Toast.LENGTH_SHORT).show();
                    stopSelf();
                }

                mHandler.postDelayed(mRunnable,  10*1000);
            }
        };
        mHandler.postDelayed(mRunnable,  10*1000);

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    private void sendTrackInfo(final String recivers){

        final LocationTracker locationTracker = new LocationTracker(getApplicationContext());
        myprefs = new Myprefs(getApplicationContext());
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(locationTracker.getLatitude() != 0 && locationTracker.getLongitude() != 0){
                    Toast.makeText(getApplicationContext(), "Service Location", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), String.valueOf(recivers), Toast.LENGTH_SHORT).show();
                    Log.d("datasent", String.valueOf(locationTracker.getLatitude())+" "+String.valueOf(locationTracker.getLongitude())+" "+String.valueOf(recivers));
                    Connection conn = new Connection();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("lat", String.valueOf(locationTracker.getLatitude()));
                    map.put("lng", String.valueOf(locationTracker.getLongitude()));
                    map.put("username", myprefs.getUserData().get("username"));
                    map.put("sender", myprefs.getUserData().get("userid"));
                    map.put("recivers", recivers);
                    conn.makeRequest(LinkUrls.trackMe, map, new Connection.GetResponse() {
                        @Override
                        public void response(final String response) {
                            Log.d("responsetrackme", response);
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!response.equals("error")){

                                    }else{
                                        //internet connection error/failure to connect to server
                                    }
                                }
                            });

                        }
                    });
                }else{
                    //Toast.makeText(getApplicationContext(), "Service Failed", Toast.LENGTH_SHORT).show();
                    sendTrackInfo(recivers);
                }
            }
        });

    }
}
