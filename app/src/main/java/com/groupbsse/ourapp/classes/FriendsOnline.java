package com.groupbsse.ourapp.classes;

import com.orm.SugarRecord;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class FriendsOnline extends SugarRecord {

    String myid,name,number,image;

    public FriendsOnline() {
    }

    public FriendsOnline(String myid, String name, String number) {
        this.myid = myid;
        this.name = name;
        this.number = number;
        this.image = image;
    }

    public String getMyid() {
        return myid;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getImage() {
        return image;
    }
}
