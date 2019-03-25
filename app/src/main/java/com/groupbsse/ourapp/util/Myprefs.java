package com.groupbsse.ourapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.groupbsse.ourapp.classes.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sayrunjah on 13/12/2016.
 */
public class Myprefs {

    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_USER = "user_id";
    private static final String TAG_NAME = "username";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_FIREBASE_TOKEN = "token";
    private static final String TAG_IS_RUNNING = "mainactivity";
    private static final String TAG_NOTIFICATION_MESSAGE = "notificationMessage";
    private static final String TAG_NOTIFICATION_COUNT = "notificationCount";
    private static final String TAG_INIT = "init";
    private static final String TAG_TRACK_ME_USERS = "track_me_";

    public static Myprefs mInstance;
    public static Context mCtx;

    public Myprefs(Context context) {
        mCtx = context;
    }

    public static synchronized Myprefs getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Myprefs(context);
        }
        return mInstance;
    }
    public void saveUserId(String user_id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_USER, user_id);
        editor.apply();
    }


    public void saveUserData(String id,String name,String phone,String image){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_USER, id);
        editor.putString(TAG_NAME, name);
        editor.putString(TAG_PHONE, phone);
        editor.putString(TAG_IMAGE, image);
        editor.apply();
    }

    public void clearUserData(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(TAG_USER).commit();
        sharedPreferences.edit().remove(TAG_NAME).commit();
        sharedPreferences.edit().remove(TAG_IMAGE).commit();
        sharedPreferences.edit().remove(TAG_PHONE).commit();
    }

    public HashMap<String,String> getUserData(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        HashMap<String,String> map = new HashMap<>();
        map.put("userid",sharedPreferences.getString(TAG_USER, null));
        map.put("username",sharedPreferences.getString(TAG_NAME, null));
        map.put("phone",sharedPreferences.getString(TAG_PHONE, null));
        map.put("image",sharedPreferences.getString(TAG_IMAGE, null));
        return map;
    }

    public void saveValueRunning(boolean isRunning){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_IS_RUNNING, isRunning);
        editor.apply();

    }

    public boolean getValueRunning(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(TAG_IS_RUNNING, false);
    }


    public void storeToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_FIREBASE_TOKEN, token);
        editor.apply();
    }

    public  String getToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_FIREBASE_TOKEN, null);
    }

    public void storeNotificationMessageInfo(String message,int count){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_NOTIFICATION_MESSAGE, message);
        editor.putInt(TAG_NOTIFICATION_COUNT,count);
        editor.apply();
    }

    public String getNotificationMessage(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_NOTIFICATION_MESSAGE, "");
    }

    public int getNotificationCount(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt(TAG_NOTIFICATION_COUNT, 0);
    }

    public void clearNotificationInfo(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(TAG_NOTIFICATION_MESSAGE).commit();
        sharedPreferences.edit().remove(TAG_NOTIFICATION_COUNT).commit();
    }

    public void init(boolean init){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_INIT, init);
        editor.apply();
    }

    public boolean isInit(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(TAG_INIT, false);
    }

    public void storeTrackMeUsers(String recivers){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TRACK_ME_USERS, recivers);
        editor.apply();
    }

    public String getTrackMeRecivers(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TRACK_ME_USERS, null);
    }
}
