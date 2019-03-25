package com.groupbsse.ourapp;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.Myprefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }

    Connection connection;
    CommonUtils commonUtils;
    Myprefs myprefs;

    EditText username,email,phone,password, cpasswrord;
    String Username,Email,Phone,Password, Cpassword;
    CardView signup;
    TextView login;

    ProgressDialog progressDialog;
    String token;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        login = (TextView)rootView.findViewById(R.id.login);
        username = (EditText)rootView.findViewById(R.id.username);
        email = (EditText)rootView.findViewById(R.id.email);
        phone = (EditText)rootView.findViewById(R.id.phone_edt);
        password = (EditText)rootView.findViewById(R.id.password);
        cpasswrord = (EditText)rootView.findViewById(R.id.cpassword);
        signup = (CardView)rootView.findViewById(R.id.signup);

        commonUtils = new CommonUtils(getActivity());
        connection = new Connection();
        myprefs = new Myprefs(getActivity());



        /*LocalBroadcastManager.getInstance(getActivity()).registerReceiver(tokenReceiver,
                new IntentFilter("tokenReceiver"));*/



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Username = username.getText().toString();
                Email = email.getText().toString();
                Phone = phone.getText().toString();
                Password = password.getText().toString();
                Cpassword = cpasswrord.getText().toString();
                if(Username.trim().equals("") || Email.trim().equals("") || Phone.trim().equals("") || Password.trim().equals("")){
                    commonUtils.MessageDialog("Please fill in all fields", null, new CommonUtils.MyAction() {
                        @Override
                        public void actionPerfomed() {

                        }

                        @Override
                        public void actionCancel() {

                        }
                    });
                }else{
                    if(Phone.length() < 10 || Phone.length() > 13){
                        commonUtils.MessageDialog("Invalid phone number", null, new CommonUtils.MyAction() {
                            @Override
                            public void actionPerfomed() {

                            }

                            @Override
                            public void actionCancel() {

                            }
                        });
                    }else{
                        if(!(Password.equals(Cpassword))){
                            Toast.makeText(getActivity(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                            password.setText("");
                            cpasswrord.setText("");
                        }else{
                            performRegister(Username,Email,Phone,Password,myprefs.getToken());
                        }


                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoSiActivity act= (LoSiActivity)getActivity();
                LoginFragment fragment= new LoginFragment();
                FragmentManager fm= act.getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.frame_container, fragment, "Login").commit();
            }
        });


        return rootView;
    }

    @Override
    public void onResume() {


        super.onResume();
    }


    public void performRegister(String name,String email,String phone,String password,String token){
        progressDialog = ProgressDialog.show(RegisterFragment.this.getActivity(),null,"Please wait...");
        HashMap<String ,String> map = new HashMap<>();
        map.put("name",name);
        map.put("email",email);
        map.put("phone",phone);
        map.put("password",password);
        map.put("token",token);
        connection.makeRequest(LinkUrls.registerUser, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("respones",response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null)
                            progressDialog.dismiss();

                        String message = "";
                        String success = "";

                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                  message = jsonObject.getString("message");
                                    success = jsonObject.getString("success");
                                }

                                final String finalSuccess = success;
                                commonUtils.MessageDialog(message, null, new CommonUtils.MyAction() {
                                    @Override
                                    public void actionPerfomed() {
                                        if(finalSuccess.equals("1")){
                                            LoSiActivity act= (LoSiActivity)getActivity();
                                            LoginFragment fragment= new LoginFragment();
                                            FragmentManager fm= act.getSupportFragmentManager();
                                            fm.beginTransaction()
                                                    .replace(R.id.frame_container, fragment, "Login").commit();
                                        }else{

                                        }

                                    }

                                    @Override
                                    public void actionCancel() {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            commonUtils.MessageDialog("Check connection and try again", null, new CommonUtils.MyAction() {
                                @Override
                                public void actionPerfomed() {

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

    }
}
