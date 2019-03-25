package com.groupbsse.ourapp.classes;

import com.orm.SugarRecord;

/**
 * Created by Sayrunjah on 23/06/2017.
 */
public class TrackMe extends SugarRecord{
    public String  location, time, lat, lng, sender;

    public TrackMe() {
    }

    public TrackMe(String location, String time, String lat, String lng, String sender) {
        this.location = location;
        this.time = time;
        this.lat = lat;
        this.lng = lng;
        this.sender = sender;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getSender() {
        return sender;
    }
}
