package com.groupbsse.ourapp.classes;

import com.orm.SugarRecord;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class FriendClass extends SugarRecord {
    String userid,name,number,image, table_id,fid;
    String desc;

    public FriendClass() {
    }

    public FriendClass(String userid, String desc) {
        this.userid = userid;
        this.desc = desc;
    }

    public FriendClass(String userid, String name, String number, String image, String fid) {
        this.userid = userid;
        this.name = name;
        this.number = number;
        this.image = image;
        this.fid = fid;
    }

    public String getUserid() {
        return userid;
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

    public String getTable_id() {
        return table_id;
    }

    public String getDesc() {
        return desc;
    }

    public String getFid() {
        return fid;
    }
}
