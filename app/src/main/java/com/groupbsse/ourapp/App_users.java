package com.groupbsse.ourapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.groupbsse.ourapp.adapters.SearcUserAdapter;
import com.groupbsse.ourapp.classes.Contact;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.Myprefs;
import com.groupbsse.ourapp.util.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class App_users extends Fragment {


    public App_users() {
        // Required empty public constructor
    }


    RecyclerView recyclerView;
    SearcUserAdapter searcUserAdapter;
    ArrayList<Contact> arrayList;
    Connection connection;
    CommonUtils commonUtils;
    Myprefs myprefs;

    ProgressDialog progressDialog;
    EditText search;
    Button enter;
    CardView empty_card;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_app_users, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        connection = new Connection();
        commonUtils = new CommonUtils(App_users.this.getActivity());
        myprefs = new Myprefs(getActivity());

        search = (EditText)rootView.findViewById(R.id.search);
        enter = (Button) rootView.findViewById(R.id.enter);
        empty_card = (CardView)rootView.findViewById(R.id.emtpy_card);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mysearch = search.getText().toString();
                if(arrayList == null)
                    arrayList = new ArrayList<>();
                else if(arrayList.size() > 0)
                    arrayList.clear();

                if(mysearch.length() < 10 || mysearch.length() > 13){
                    Toast.makeText(getActivity(),"Use valid phone number for better search",Toast.LENGTH_SHORT).show();
                }else {
                    if(mysearch.trim().equals("")){
                        Toast.makeText(getActivity(),"Please enter some text",Toast.LENGTH_SHORT).show();
                    }else{
                        performSearch(mysearch,myprefs.getUserData().get("userid"));
                    }
                }



            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
         recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
             @Override
             public void onClick(View view, int position) {
                   final Contact contact = arrayList.get(position);
                 if(contact.isApp_user()!= 1 && contact.isApp_user() != 0){
                     commonUtils.ConfirmationDialog("Send Request", null, new CommonUtils.MyAction() {
                         @Override
                         public void actionPerfomed() {
                             addFriend(myprefs.getUserData().get("userid"),contact.getContact_id());
                         }

                         @Override
                         public void actionCancel() {

                         }
                     });
                 }

             }

             @Override
             public void onLongClick(View view, int position) {

             }
         }));
        super.onResume();
    }

    public void performSearch(String number,String userid){
       progressDialog = ProgressDialog.show(App_users.this.getActivity(),null,"Please wait");
        HashMap<String ,String> map = new HashMap<>();
        map.put("number",number);
        map.put("userid",userid);
        map.put("username",myprefs.getUserData().get("username"));
        map.put("command","search");
        connection.makeRequest(LinkUrls.searchUser, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("respones",response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null)
                            progressDialog.dismiss();

                        String id,name,number,message,succes;
                        int status = 0;

                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    succes = jsonObject.getString("success");
                                    if(succes.equals("1")){
                                        id = jsonObject.getString("id");
                                        name = jsonObject.getString("name");
                                        number = jsonObject.getString("number");
                                        status = Integer.valueOf(jsonObject.getString("status"));
                                        arrayList.add(new Contact(id,name,number,status));
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(arrayList.size() > 0){
                                searcUserAdapter = new SearcUserAdapter(getActivity(),arrayList);
                                recyclerView.setAdapter(searcUserAdapter);
                                empty_card.setVisibility(View.GONE);
                            }else{
                               empty_card.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                });
            }
        });
    }

    public void addFriend(String userid, String fid){
        progressDialog = ProgressDialog.show(App_users.this.getActivity(),null,"Please wait");
        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        map.put("fid",fid);
        map.put("username",myprefs.getUserData().get("username"));
        map.put("mynumber",new Myprefs(getActivity()).getUserData().get("phone"));
        map.put("image",new Myprefs(getActivity()).getUserData().get("image"));
        map.put("command","addfriend");
        connection.makeRequest(LinkUrls.searchUser, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("respones",response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null)
                            progressDialog.dismiss();

                        String message = null,succes;

                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    succes = jsonObject.getString("success");
                                    if(succes.equals("1")){
                                       message = jsonObject.getString("message");
                                    }
                                }
                                commonUtils.MessageDialog("Succefull", null, new CommonUtils.MyAction() {
                                    @Override
                                    public void actionPerfomed() {
                                        if(arrayList == null)
                                            arrayList = new ArrayList<>();
                                        else if(arrayList.size() > 0)
                                            arrayList.clear();

                                        search.setText("");
                                        searcUserAdapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(searcUserAdapter);

                                    }

                                    @Override
                                    public void actionCancel() {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
    }
}
