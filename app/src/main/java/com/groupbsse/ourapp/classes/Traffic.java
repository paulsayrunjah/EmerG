package com.groupbsse.ourapp.classes;

import java.util.ArrayList;

/**
 * Created by Sayrunjah on 09/02/2017.
 */
public class Traffic {

    String traffic_id,user_id,description,image;
    ArrayList<String> recivers;

    public Traffic(String traffic_id, String user_id, String description, String image, ArrayList<String> recivers) {
        this.traffic_id = traffic_id;
        this.user_id = user_id;
        this.description = description;
        this.image = image;
        this.recivers = recivers;
    }

    public String getTraffic_id() {
        return traffic_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<String> getRecivers() {
        return recivers;
    }
}
