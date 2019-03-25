package com.groupbsse.ourapp.classes;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class MeetUp extends SugarRecord {

    String meet_up_id,description,location_name,user_id,sender_name,image;
    Double latitude,longitude;
    ArrayList<String> recievers;
    boolean seen;

    public MeetUp() {
    }




    public MeetUp(String meet_up_id, String description, String location_name, Double latitude, Double longitude, String sender_name, String image) {
        this.meet_up_id = meet_up_id;
        this.description = description;
        this.location_name = location_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sender_name = sender_name;
        this.image = image;
    }

    public MeetUp(String meet_up_id, String description, String location_name, Double latitude, Double longitude, String sender_name, String image,boolean seen) {
        this.meet_up_id = meet_up_id;
        this.description = description;
        this.location_name = location_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sender_name = sender_name;
        this.image = image;
        this.seen = seen;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getImage() {
        return image;
    }

    public String getMeet_up_id() {
        return meet_up_id;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation_name() {
        return location_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public ArrayList<String> getRecievers() {
        return recievers;
    }
}
