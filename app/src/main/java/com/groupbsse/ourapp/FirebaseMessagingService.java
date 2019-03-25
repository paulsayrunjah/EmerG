package com.groupbsse.ourapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.groupbsse.ourapp.classes.AlertMessage;
import com.groupbsse.ourapp.classes.FriendClass;
import com.groupbsse.ourapp.classes.MeetUp;
import com.groupbsse.ourapp.classes.NotificationClass;
import com.groupbsse.ourapp.classes.TrackMe;
import com.groupbsse.ourapp.classes.Warning;
import com.groupbsse.ourapp.util.Myprefs;

import org.json.JSONException;
import org.json.JSONObject;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    Myprefs myprefs;
    static int notifyID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        myprefs = new Myprefs(getApplicationContext());
        String message = myprefs.getNotificationMessage();
        Log.d("Message :","Received ");
        try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("data"));
            message = myprefs.getNotificationMessage() + jsonObject.getString("message")+"\n";
            myprefs.storeNotificationMessageInfo(message,myprefs.getNotificationCount()+1);
            saveNotifications(jsonObject);
            showNotification(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showNotification(JSONObject jsonObject) {
          if(myprefs.getNotificationCount() == 1){
              try {
                  notifyOps(myprefs.getNotificationMessage(),jsonObject.getString("task"));
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }else{
              try {
                  updateNotification(myprefs.getNotificationMessage(),jsonObject.getString("task"));
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }

    }

    public void saveNotifications(JSONObject jsonObject){
        try {
            String type = jsonObject.getString("task");
            if(type.equals("alert")){
                String sender = jsonObject.getString("sender");
                String messageid = jsonObject.getString("messageid");
                String time = jsonObject.getString("time");
                String desc = jsonObject.getString("desc");
                double lat = jsonObject.getDouble("lat");
                double lng = jsonObject.getDouble("lng");
                String location = jsonObject.getString("location");

                AlertMessage alert_message = new AlertMessage(messageid,"1",desc,location,lat,lng,sender,time,false);
                alert_message.save();
            }else if(type.equals("meet")){
                String sender = jsonObject.getString("sender");
                String messageid = jsonObject.getString("messageid");
                String desc = jsonObject.getString("desc");
                double lat = jsonObject.getDouble("lat");
                double lng = jsonObject.getDouble("lng");
                String location = jsonObject.getString("location");
                String image = jsonObject.getString("image");

                MeetUp meet_up = new MeetUp(messageid,desc,location,lat,lng,sender,image,false);
                meet_up.save();

            }else if(type.equals("warn")){
                String sender = jsonObject.getString("sender");
                String messageid = jsonObject.getString("messageid");
                String desc = jsonObject.getString("desc");
                String time = jsonObject.getString("time");

                Warning warning = new Warning(messageid,desc,time,sender,false);
                warning.save();
            }else if(type.equals("friend")){
                String userid = jsonObject.getString("userid");
                String message = jsonObject.getString("message");
                String name = jsonObject.getString("name");
                String number = jsonObject.getString("mynumber");
                String image = jsonObject.getString("image");

                /*FriendClass friendClass = new FriendClass(userid,name,number,image,"1");
                friendClass.save();*/

            }else if(type.equals("trackme")){
                 String location = jsonObject.getString("location");
                String lng = jsonObject.getString("lng");
                String lat = jsonObject.getString("lat");
                String time = jsonObject.getString("time");
                String sender = jsonObject.getString("sender");

                TrackMe trackMe = new TrackMe(location, time, lat, lng, sender);
                trackMe.save();
            }

          /*  NotificationClass notificationClass = new NotificationClass(type,notificationid,simpledesc,time,sender,"0");
            notificationClass.save()*/;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notifyOps(String content,String task){
        NotificationManager manager;
        NotificationCompat.Builder builder;
        Uri sound = Uri.parse("android.resource://" + getPackageName()+"/" + R.raw.sound);

        Intent i = null;

        if(task.equals("friend")){
            i = new Intent(this, MainActivity.class);
            i.putExtra("position",3);
            i.putExtra("action","friend");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }else if(task.equals("trackme")){
            i = new Intent(this, MainActivity.class);
            i.putExtra("position",1);
            i.putExtra("action","trackme");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }else{
            i = new Intent(this, MainActivity.class);
            i.putExtra("action","notify");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
       /* i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);*/


       /* if(task.equals("meet")){
            i = new Intent(this, MainActivity.class);
            i.putExtra("position",1);

        }else if(task.equals("warn")){
            i = new Intent(this, MainActivity.class);
            i.putExtra("position",2);
        }else if(task.equals("alert")){
            i = new Intent(this, MainActivity.class);
            i.putExtra("position",0);
        }else if(task.equals("friend")){
            i = new Intent(this, MainActivity.class);
            i.putExtra("position",3);
        }*/


        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),1,i,PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Wetaase")
                .setSmallIcon(R.drawable.app_icon)
                .setOngoing(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(content);
        builder.setStyle(bigText);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        manager.notify(notifyID,builder.build());
    }

    private void updateNotification(String content,String task){
        this.notifyOps(content,task);
    }


}
