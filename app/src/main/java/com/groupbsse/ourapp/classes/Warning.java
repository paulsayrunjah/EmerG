package com.groupbsse.ourapp.classes;

import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by Sayrunjah on 09/02/2017.
 */
public class Warning extends SugarRecord {
    String warning_id,description,user_id,time;
    String image,audio,sender;
    ArrayList<String> recievers;
    ArrayList<LatLng> cords;
    public boolean seen;


    public Warning() {
    }

    public Warning(String warning_id, String description, String time,String sender, boolean seen) {
        this.warning_id = warning_id;
        this.description = description;
        this.time = time;
        this.sender = sender;
        this.seen = seen;
    }

    public Warning(String warning_id, String description, String user_id, String time, ArrayList<String> recievers) {
        this.warning_id = warning_id;
        this.description = description;
        this.user_id = user_id;
        this.time = time;
        this.recievers = recievers;
    }

    public Warning(String warning_id,String description, String time, ArrayList<String> recievers, ArrayList<LatLng> cords) {
        this.description = description;
        this.time = time;
        this.recievers = recievers;
        this.cords = cords;
        this.warning_id = warning_id;
    }

    public Warning(String warning_id, String description, String time, String image, String audio, ArrayList<String> recievers) {
        this.warning_id = warning_id;
        this.description = description;
        this.time = time;
        this.image = image;
        this.audio = audio;
        this.recievers = recievers;
        this.cords = cords;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getSender() {
        return sender;
    }

    public String getImage() {
        return image;
    }

    public String getAudio() {
        return audio;
    }

    public ArrayList<LatLng> getCords() {
        return cords;
    }

    public String getWarning_id() {
        return warning_id;
    }

    public String getDescription() {
        return description;
    }

    public String getUser_id() {
        return user_id;
    }

    public ArrayList<String> getRecievers() {
        return recievers;
    }

    public String getTime() {
        return time;
    }
}
