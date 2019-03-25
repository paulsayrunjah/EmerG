package com.groupbsse.ourapp.classes;

import com.orm.SugarRecord;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class Contacts extends SugarRecord {

    String number,name,userid;

    public Contacts() {
    }

    public Contacts(String number, String name, String userid) {
        this.number = number;
        this.name = name;
        this.userid = userid;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getUserid() {
        return userid;
    }
}
