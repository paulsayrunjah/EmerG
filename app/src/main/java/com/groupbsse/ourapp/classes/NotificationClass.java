package com.groupbsse.ourapp.classes;

import com.orm.SugarRecord;

/**
 * Created by Sayrunjah on 13/04/2017.
 */
public class NotificationClass extends SugarRecord {

    String type;
    String notificationid;
    String desc;
    String time;
    String sender;
    String status;

    public NotificationClass() {
    }

    public NotificationClass(String type, String notificationid, String desc, String time, String sender,String status) {
        this.type = type;
        this.notificationid = notificationid;
        this.desc = desc;
        this.time = time;
        this.sender = sender;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getNotificationid() {
        return notificationid;
    }

    public String getDesc() {
        return desc;
    }

    public String getTime() {
        return time;
    }
}
