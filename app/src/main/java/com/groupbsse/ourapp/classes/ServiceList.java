package com.groupbsse.ourapp.classes;

import android.util.SparseBooleanArray;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayrunjah on 20/12/2016.
 */
public class ServiceList {

    public static ArrayList<Contact> my_contacts = new ArrayList<>();
    public static SparseBooleanArray selected_positions = new SparseBooleanArray();
    public static ArrayList<Integer> my_positions = new ArrayList<>();
   // public static List<ArrayList<Contact>> my_sche_con = new ArrayList<>();
    public static ArrayList<Contact> my_fav_contacts = new ArrayList<>();
    public static ArrayList<String> customMessage = new ArrayList<>();

    public static List<ArrayList<LatLng>> cords = new ArrayList<>();
    public static ArrayList<Users> usersArrayList = new ArrayList<>();

    public static ArrayList<Users> recieversList = new ArrayList<>();


    public static void ClearLists(){
        my_contacts.clear();
        selected_positions.clear();
        my_positions.clear();
    }

}
