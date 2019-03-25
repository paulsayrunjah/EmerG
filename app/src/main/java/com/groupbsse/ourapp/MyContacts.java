package com.groupbsse.ourapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.groupbsse.ourapp.adapters.ContactAdapter;
import com.groupbsse.ourapp.adapters.OtherContactAdapter;
import com.groupbsse.ourapp.classes.Contact;
import com.groupbsse.ourapp.classes.Contacts;
import com.groupbsse.ourapp.classes.FriendsOnline;
import com.groupbsse.ourapp.classes.ServiceList;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.Myprefs;
import com.groupbsse.ourapp.util.RecyclerSwipeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyContacts extends Fragment {


    public MyContacts() {
        // Required empty public constructor
    }

    int CONTACT_PICK_REQUEST = 11;
    TextView empty;
    ContactAdapter contactAdapter;
    OtherContactAdapter otherContactAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutmanager;

    CommonUtils commonUtils;

    Myprefs prefs;
    String listofContacts;
    List<Contacts> contactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_contacts, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        layoutmanager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutmanager);
        empty = (TextView)rootView.findViewById(R.id.empty);
       commonUtils = new CommonUtils(MyContacts.this.getActivity());

        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContacts = new Intent(getActivity(),ContactsPickerActivity.class);
                startActivityForResult(pickContacts,CONTACT_PICK_REQUEST);
            }
        });
        //new doInBackground().execute();
        return rootView;
    }

    @Override
    public void onResume() {

        ItemTouchHelper.Callback callback = new RecyclerSwipeListener(otherContactAdapter, new RecyclerSwipeListener.SwipeListener() {
            @Override
            public void onSiwpe(final int position) {
                final Contacts contacts = contactList.get(position);
                otherContactAdapter.notifyDataSetChanged();
                new CommonUtils(MyContacts.this.getActivity()).ConfirmationDialog("Deleting " + contacts.getNumber(), null, new CommonUtils.MyAction() {
                    @Override
                    public void actionPerfomed() {
                        Contacts contacts1 = Contacts.findById(Contacts.class, contacts.getId());
                        contacts1.delete();
                        otherContactAdapter.remove(position);
                    }

                    @Override
                    public void actionCancel() {

                    }
                });
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        prefs = new Myprefs(getActivity());
       /* if(getprefList().size() > 0){
            ServiceList.my_fav_contacts = getprefList();
        }*/
        ListContacts();
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICK_REQUEST && resultCode == getActivity().RESULT_OK){
            ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");
            Log.d("Saved",String.valueOf(selectedContacts.size()));
            for(int i=0;i<selectedContacts.size();i++){
                String number = selectedContacts.get(i).getNumber();
                if(!(commonUtils.formartMobileNumber(number).equals("error"))){
                    Contacts contacts_added = new Contacts(commonUtils.formartMobileNumber(number),selectedContacts.get(i).getName(),new Myprefs(getActivity()).getUserData().get("userid"));
                    List<Contacts> contactlist = Contacts.find(Contacts.class, "userid = ? and number = ?",new Myprefs(getActivity()).getUserData().get("userid"),contacts_added.getNumber());
                    Log.d("Saved",String.valueOf(contactlist.size()));
                    if(contactlist.size() > 0){
                        Toast.makeText(getActivity(),number+" is already addded to contacts" ,Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("Saved","Truee");
                        contacts_added.save();
                    }
                }else{
                    Toast.makeText(getActivity(),number+" is not a valid mobile number" ,Toast.LENGTH_SHORT).show();

                }

            }
        }
    }



    public void ListContacts(){
        contactList = Contacts.find(Contacts.class,"userid = ?",new Myprefs(getActivity()).getUserData().get("userid"));
        for(Contacts c : contactList){
            Log.d("Name :"+c.getName(),"Number :"+c.getNumber());
        }
        if(contactList.size() < 0){
            empty.setVisibility(View.VISIBLE);
        }else{
               empty.setVisibility(View.GONE);
            otherContactAdapter = new OtherContactAdapter(getActivity(),contactList);
                recyclerView.setAdapter(otherContactAdapter);
        }
    }




}
