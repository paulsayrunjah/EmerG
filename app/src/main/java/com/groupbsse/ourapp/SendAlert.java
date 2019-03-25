package com.groupbsse.ourapp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.github.clans.fab.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.groupbsse.ourapp.adapters.CustomMessageAdapter;
import com.groupbsse.ourapp.classes.Contacts;
import com.groupbsse.ourapp.classes.CustomMessage;
import com.groupbsse.ourapp.classes.FriendsOnline;
import com.groupbsse.ourapp.classes.MyLocation;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.LocationFromWeb;
import com.groupbsse.ourapp.util.LocationTracker;
import com.groupbsse.ourapp.util.Myprefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendAlert extends Fragment {


   Spinner spinner;

    double lat,lng;
    String address;
    LocationTracker locationTracker;

    JSONArray jsonArray;
    JSONObject jsonObject1;

    Button send_alert;
    Myprefs myprefs;

    CommonUtils commonUtils;
    CustomMessageAdapter customMessageAdapter;

    ProgressDialog progressDialog;
    String physicaladdress, returnedAdd;
    boolean valueGot = false;


    private Boolean isFabOpen = false;
    private FloatingActionMenu fam;
    private FloatingActionButton fab3,fab1,fab2;
   /* private Animation fab_open,fab_close,rotate_forward,rotate_backward;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_send_alert, container, false);
        spinner = (Spinner)rootView.findViewById(R.id.spinner);
        send_alert = (Button)rootView.findViewById(R.id.send_alert);
        locationTracker = new LocationTracker(getActivity());
        fab1 = (FloatingActionButton)rootView.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)rootView.findViewById(R.id.fab2);
        fam = (FloatingActionMenu)rootView.findViewById(R.id.fab_menu);
        myprefs = new Myprefs(getActivity());
        commonUtils = new CommonUtils(getActivity());

        send_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = spinner.getSelectedItem().toString();
                CustomMessage customMessage = (CustomMessage) spinner.getSelectedItem();
               // Toast.makeText(getActivity(),customMessage.getMessage()+ " "+customMessage.getType(),Toast.LENGTH_SHORT).show();
                progressDialog = ProgressDialog.show(SendAlert.this.getActivity(),null,"Please wait..");
                sendReport(customMessage.getMessage(), customMessage.getType());
            }
        });
        //handling menu status (open or close)
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    fam.setLayoutParams(new FrameLayout.LayoutParams(com.github.clans.fab.FloatingActionMenu.LayoutParams.MATCH_PARENT, com.github.clans.fab.FloatingActionMenu.LayoutParams.MATCH_PARENT));
                    //Toast.makeText(getActivity(),"Menu is open",Toast.LENGTH_SHORT).show();
                } else {
                    fab1.setVisibility(View.GONE);
                    fab2.setVisibility(View.GONE);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(com.github.clans.fab.FloatingActionMenu.LayoutParams.WRAP_CONTENT, com.github.clans.fab.FloatingActionMenu.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                    fam.setLayoutParams(params);
                   // Toast.makeText(getActivity(),"Menu is open",Toast.LENGTH_SHORT).show();
                }
            }
        });




        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                Dialog dialog = new Dialog(SendAlert.this.getActivity());
                dialog.setTitle("Message");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.custom_message_list);
                RecyclerView recyclerView = (RecyclerView)dialog.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(SendAlert.this.getActivity()));
                List<CustomMessage> messages = CustomMessage.findWithQuery(CustomMessage.class,"select * from CUSTOM_MESSAGE where userid = ?",myprefs.getUserData().get("userid"));
                customMessageAdapter = new CustomMessageAdapter(SendAlert.this.getActivity(),messages);
                recyclerView.setAdapter(customMessageAdapter);
                dialog.show();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                final Dialog dialog = new Dialog(SendAlert.this.getActivity());
                dialog.setTitle("Add Custom Message");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_custom_message);
                final EditText message = (EditText)dialog.findViewById(R.id.message);
                Button ok = (Button)dialog.findViewById(R.id.ok);
                Button cancel = (Button)dialog.findViewById(R.id.cancel);


                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.radiogroup);
                        String m = message.getText().toString();
                        String type = "";
                        if(m.equals("")){
                            Toast.makeText(getActivity(),"please add a text",Toast.LENGTH_SHORT).show();
                        }else{
                            // get selected radio button from radioGroup
                            int selectedId = radioGroup.getCheckedRadioButtonId();

                            // find the radiobutton by returned id
                           RadioButton radioButton = (RadioButton)dialog.findViewById(selectedId);
                            if(radioButton.getText().equals("Health")){
                                type = "1";
                            }else if(radioButton.getText().equals("Police")){
                                type = "2";
                            }else if(radioButton.getText().equals("Fire")){
                                type = "3";
                            }

                         /*   Toast.makeText(getActivity(),
                                    radioButton.getText()+" "+String.valueOf(selectedId), Toast.LENGTH_SHORT).show();*/
                            CustomMessage customMessage = new CustomMessage(m,myprefs.getUserData().get("userid"),type);
                            customMessage.save();
                        }
                        addItemsOnSpinner2();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        addItemsOnSpinner2();
        super.onResume();
    }


    public void addItemsOnSpinner2() {
        List<CustomMessage> allmessages = new ArrayList<>();
        List<CustomMessage> defaultmessages = CustomMessage.findWithQuery(CustomMessage.class,"select * from CUSTOM_MESSAGE where userid = ?","0");
        allmessages.addAll(defaultmessages);

        if(myprefs.getUserData().get("userid") != null){
            List<CustomMessage> messages = CustomMessage.findWithQuery(CustomMessage.class,"select * from CUSTOM_MESSAGE where userid = ?",myprefs.getUserData().get("userid"));
            allmessages.addAll(messages);
        }
        ArrayAdapter<CustomMessage> dataAdapter = new ArrayAdapter<CustomMessage>(getActivity(),
                android.R.layout.simple_spinner_item, allmessages);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    public void sendReport(final String desc,final String type){

        if(progressDialog == null){
            progressDialog = ProgressDialog.show(SendAlert.this.getActivity(),null,"Please wait..");
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("location:", String.valueOf(locationTracker.getLatitude())+" "+String.valueOf(locationTracker.getLongitude()));
                if(locationTracker.getLatitude() != 0 && locationTracker.getLatitude() != 0){
                    String latlng = locationTracker.getLatitude()+","+locationTracker.getLongitude();
                    //new sendTextMessage().execute(locationTracker.getPhysicalAddFromWeb(latlng));
                    HashMap<String,String> map = new HashMap<>();
                    map.put("userid",myprefs.getUserData().get("userid"));
                    map.put("username",new Myprefs(getActivity()).getUserData().get("username"));
                    map.put("desc",desc);
                    map.put("lat",String.valueOf(locationTracker.getLatitude()));
                    map.put("lng",String.valueOf(locationTracker.getLongitude()));
                    //map.put("locationname",s.get(0).getLocationname());
                    map.put("contacts",String.valueOf(commonUtils.getMyContactList()));
                    map.put("type", type);
                    Log.d("contacts",String.valueOf(commonUtils.getMyContactList()));
                    Connection response = new Connection();
                    response.makeRequest(LinkUrls.sendAlert, map, new Connection.GetResponse() {
                        @Override
                        public void response(final String response) {
                            Log.d("response",response);
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(progressDialog != null){
                                        progressDialog.dismiss();
                                    }
                                    String message = "", locationuser = "";
                                    if(!response.equals("error")){
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            for(int i=0; i <jsonArray.length();i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                locationuser = jsonObject.getString("location");
                                                if(jsonObject.getString("success").equals("1")){
                                                    message = jsonObject.getString("message");
                                                    new sendTextMessage().execute(desc,locationuser);
                                                    commonUtils.MessageDialog("message sent", null, new CommonUtils.MyAction() {
                                                        @Override
                                                        public void actionPerfomed() {

                                                        }

                                                        @Override
                                                        public void actionCancel() {

                                                        }
                                                    });

                                                }else{
                                                    new sendTextMessage().execute(desc,locationuser);
                                                    commonUtils.MessageDialog("No friends", null, new CommonUtils.MyAction() {
                                                        @Override
                                                        public void actionPerfomed() {

                                                        }

                                                        @Override
                                                        public void actionCancel() {

                                                        }
                                                    });

                                                }

                                            }

                                            Log.d("notificationsent", message);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }else{
                                        commonUtils.ConfirmationDialog("Check connection and try again", null, new CommonUtils.MyAction() {
                                            @Override
                                            public void actionPerfomed() {
                                                sendReport(desc,type);
                                            }

                                            @Override
                                            public void actionCancel() {

                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }else{
                    sendReport(desc, type);
                }
            }
        });

    }

    private ArrayList<String> contactList(){
        ArrayList<String> finalcontactList = new ArrayList<>();
       List<Contacts> contactList = Contacts.find(Contacts.class,"userid = ?",new Myprefs(getActivity()).getUserData().get("userid"));
        List<FriendsOnline> friendsOnlines = FriendsOnline.find(FriendsOnline.class,"myid = ?",new Myprefs(getActivity()).getUserData().get("userid"));
        for(Contacts cs : contactList){
            finalcontactList.add(cs.getNumber());
        }

            for(FriendsOnline fs: friendsOnlines){
                if(containsNumber(fs.getNumber(),finalcontactList) == false){
                    finalcontactList.add(fs.getNumber());
                }
            }
        for(String s: finalcontactList){
            Log.d("Number",s);
        }
        return finalcontactList;
    }

    public boolean containsNumber(final String number,ArrayList<String> contactsList) {
        for(String s : contactsList){
            if(s.equals(number)){
                return true;
            }
        }
        return false;
    }

    public void sendMessage(final String phoneNo, final String msg){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            }
        });


    }

    private class sendTextMessage extends AsyncTask<String, Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            String msg1 = "";
            if(myprefs.getUserData().get("userid") != null){
                msg1 = "Wetaase application "+"\n"+"Sender : "+myprefs.getUserData().get("username")+" \n"+"Description :"+params[0]+" \n" +"Address :"+params[1];
            }else {
                msg1 = "Wetaase application "+"\n"+params[0]+" \n" +"Address :"+params[1];
            }
            ArrayList<String> thelist =  contactList();
            if(thelist.size() > 0){
                for(int i =0; i < thelist.size(); i++){
                    sendMessage(thelist.get(i),msg1);
                    // Log.d("Sending to ...",thelist.get(i));
                }
            }

            return null;
        }
    }



}
