package com.groupbsse.ourapp.classes;

import com.orm.SugarRecord;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class CustomMessage extends SugarRecord {

    String message,userid,type;

    public CustomMessage() {
    }

    public CustomMessage(String message, String userid, String type) {
        this.message = message;
        this.userid = userid;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getUserid() {
        return userid;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return message;
    }
}
