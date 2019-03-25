package com.groupbsse.ourapp;

import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.groupbsse.ourapp.adapters.NotificationAdapter;
import com.groupbsse.ourapp.classes.NotificationClass;
import com.groupbsse.ourapp.util.Myprefs;

import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    Myprefs myprefs;
    NotificationManager manager;

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    List<NotificationClass> notificationClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        myprefs = new Myprefs(getApplicationContext());
        myprefs.clearNotificationInfo();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(FirebaseMessagingService.notifyID);

    }

    @Override
    protected void onResume() {
      /*  if(notificationClassList.size() > 0)
            notificationClassList.clear();*/

        populate();

        super.onResume();
    }

    private void populate(){
        notificationClassList = NotificationClass.find(NotificationClass.class,"status = 0");
        notificationAdapter = new NotificationAdapter(getApplicationContext(),notificationClassList);
        recyclerView.setAdapter(notificationAdapter);
    }
}
