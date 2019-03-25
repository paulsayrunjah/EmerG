package com.groupbsse.ourapp.classes;

import com.orm.SugarRecord;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class AlertMessage extends SugarRecord {
    String message_id,user_id,description,location_name;
    Double latitude,longitude;
    JSONObject recievers;
    String sender_name,time;
    public boolean seen;

    public AlertMessage() {
    }

    public AlertMessage(String message_id, String user_id, String description, String location_name, Double latitude, Double longitude, String sender_name, String time) {
        this.message_id = message_id;
        this.user_id = user_id;
        this.description = description;
        this.location_name = location_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sender_name = sender_name;
        this.time = time;
    }

    public AlertMessage(String message_id, String user_id, String description, String location_name, Double latitude, Double longitude, String sender_name, String time,boolean seen) {
        this.message_id = message_id;
        this.user_id = user_id;
        this.description = description;
        this.location_name = location_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sender_name = sender_name;
        this.time = time;
        this.seen = seen;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getTime() {
        return time;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getMessage_id() {
        return message_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation_name() {
        return location_name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public JSONObject getRecievers() {
        return recievers;
    }
}
