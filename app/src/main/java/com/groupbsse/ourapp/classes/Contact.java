package com.groupbsse.ourapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class Contact  implements Parcelable  {

    public String name,number,contact_id,label;
    public int app_user;

    public Contact(String name, String number,String userid) {
        this.name = name;
        this.number = number;
    }

    public Contact(String contact_id, String name, String number, String label){
        this.contact_id=contact_id;
        this.name=name;
        this.number=number;
        this.label=label;
    }



    public Contact(String contact_id,String name, String number, int app_user) {
        this.contact_id = contact_id;
        this.name = name;
        this.number = number;
        this.app_user = app_user;
    }


    public int isApp_user() {
        return app_user;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getContact_id() {
        return contact_id;
    }

    protected Contact(Parcel in) {
        contact_id = in.readString();
        name = in.readString();
        number = in.readString();
        label = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public String toString()
    {
        return name+" | "+label+" : "+number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contact_id);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(label);
    }

    public static String formatContact(String number){
        String format1 = number.replaceAll("\\D+", "");
        String finalformat = "256"+format1.substring(format1.length() - 9);
        return finalformat;
    }
}
