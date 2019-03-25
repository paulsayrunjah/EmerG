package com.groupbsse.ourapp.classes;

import android.util.Log;

import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;

import java.util.HashMap;

/**
 * Created by Sayrunjah on 09/02/2017.
 */
public class Users {

    String username,email,password,phone,image;
    boolean appuser;
    String userid;
    Connection connection;

    boolean isSelected;

    public Users() {
    }

    public Users(String username, String email, String password, String phone, String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;
    }

    public Users(String userid, String image, String username,String phone) {
        this.userid = userid;
        this.image = image;
        this.username = username;
        this.phone = phone;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isAppuser() {
        return appuser;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }


    public Users getUserInformation(String userId){
        HashMap<String,String> map = new HashMap<>();
        map.put("",userId);
        connection = new Connection();
        //String response = connection.makeRequest(LinkUrls.sendAlert,map);
        return  new Users("username","email","password","phone","image");
    }

    public void sendAlertMessage(AlertMessage alert_message, final ServerResponse serverResponse){
        final String result;
        HashMap<String,String> map = new HashMap<>();
        map.put("userid",alert_message.getUser_id());
        map.put("desc",alert_message.getDescription());
        map.put("lat",String.valueOf(alert_message.getLatitude()));
        map.put("lng",String.valueOf(alert_message.getLongitude()));
        map.put("locationname",alert_message.getLocation_name());
        map.put("contacts",String.valueOf(alert_message.getRecievers()));
        Log.d("contacts",String.valueOf(alert_message.getRecievers()));
        Connection response = new Connection();
        response.makeRequest(LinkUrls.sendAlert, map, new Connection.GetResponse() {
            @Override
            public void response(String response) {
               serverResponse.response(response);
               // Log.d("Response ",response);
            }
        });
    }


    public interface ServerResponse{
        void response(String response);
    }


}
