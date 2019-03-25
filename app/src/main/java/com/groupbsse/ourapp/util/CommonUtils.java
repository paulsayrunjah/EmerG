package com.groupbsse.ourapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.groupbsse.ourapp.R;
import com.groupbsse.ourapp.classes.Contact;
import com.groupbsse.ourapp.classes.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sayrunjah on 09/02/2017.
 */
public class CommonUtils {
    Context context;
    MyAction myAction;
    Activity activity;
    Myprefs myprefs;

    public CommonUtils(Context context){
        this.context = context;
    }


    public HashMap<String,String> checkEditText(List<EditText> edits){
        HashMap<String,String> map = new HashMap<>();
        for(int i=0;i <edits.size();i++){
            if(edits.get(i).getText().toString().trim().equals("")){
                map.put(edits.get(i).getText().toString(), "false");
            }else{
                map.put(edits.get(i).getText().toString(), "true");
            }
        }
        return map;
    }

    public void ConfirmationDialog(String message,String title,final MyAction myAction){


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        myAction.actionPerfomed();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        myAction.actionCancel();
                    }
                })
                .show();

    }

    public void ErrorDialog(String message,String title,final MyAction myAction){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        myAction.actionPerfomed();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        myAction.actionCancel();
                    }
                })
                .show();

    }

    public void MessageDialog(String message,String title,final MyAction myAction){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        myAction.actionPerfomed();
                    }
                })
                .show();

    }
    public interface MyAction{
        void actionPerfomed();
        void actionCancel();
    }

    public String getCurrentDate(){
        String currdate ="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        try {
            Date CurrentDate = sdf.parse(sdf.format(new Date()));
            currdate = sdf.format(CurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return currdate;
    }

    public String getTimeago(String time) {
        String ago = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        try {
            Date strDate = sdf.parse(time);
            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
            ago = prettyTime.format(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ago;
    }

    public String formartMobileNumber(String phone){
        String finalnumber = "";
        if(phone.length() < 9){
            finalnumber = "error";
        }else{
            String numberRefined = phone.replaceAll("[^0-9]", "");
            String val = numberRefined.substring(numberRefined.length() - 9);
            finalnumber = "0"+val;
        }
        return finalnumber;
    }

    //list for contacts to server
    public JSONObject getMyContactList(){
        List<Contacts> contactList = Contacts.find(Contacts.class,"userid = ?",new Myprefs(context).getUserData().get("userid"));
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        try {
            for(int i=0; i < contactList.size(); i++){
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("Name",contactList.get(i).getName());
                jsonObject.put("Number",contactList.get(i).getNumber());
                jsonArray.put(jsonObject);
            }
            jsonObject1.put("Contacts",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1;
    }

}
