package com.groupbsse.ourapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    TextView register;
    CardView login_btn,skip;
    EditText phone_edt,passwordEdt;
    ProgressDialog progressDialog;
    Myprefs myprefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        register = (TextView)rootView.findViewById(R.id.tvtReg);
        skip = (CardView)rootView.findViewById(R.id.skip);
        skip.setVisibility(View.GONE);
        phone_edt = (EditText)rootView.findViewById(R.id.phone_edt);
        passwordEdt = (EditText)rootView.findViewById(R.id.password);
        login_btn = (CardView)rootView.findViewById(R.id.btn_login);

        myprefs = new Myprefs(getActivity());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoSiActivity act= (LoSiActivity)getActivity();
                RegisterFragment fragment= new RegisterFragment();
                FragmentManager fm= act.getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.frame_container, fragment, "Register").commit();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phone_edt.getText().toString(),password = passwordEdt.getText().toString();

                if(phone.trim().equals("") || password.trim().equals("")){
                    Toast.makeText(getActivity(),"Please all fields are reqiured",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog = ProgressDialog.show(LoginFragment.this.getActivity(),null,"Logging in...");
                    if(myprefs.getToken() == null){
                        try{
                            String token = FirebaseInstanceId.getInstance().getToken();
                            myprefs.storeToken(token);
                            LoginUser(phone,password,myprefs.getToken());
                        }catch (Exception e){
                            Toast.makeText(getActivity()," Check internet connection ",Toast.LENGTH_LONG).show();
                        }

                    }else{
                        LoginUser(phone,password,myprefs.getToken());
                    }

                }

            }
        });
        return rootView;
    }

    public void LoginUser(String phone,String password,String token){
        Connection connection = new Connection();
        final CommonUtils commonUtils = new CommonUtils(LoginFragment.this.getActivity());
        HashMap<String, String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("password",password);
        map.put("token",token);
        connection.makeRequest(LinkUrls.loginUser, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("Response",response);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            if(progressDialog != null)
                                progressDialog.dismiss();

                            String success = null,message = null,image = null,phone = null,id = null,name = null;
                            if(!response.equals("error")){
                                try {
                                    JSONArray jsonArray = new JSONArray(response);

                                    for(int i =0; i <jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        success = jsonObject.getString("success");
                                        message = jsonObject.getString("message");
                                        if(success.equals("1")){
                                            image = jsonObject.getString("image");
                                            phone = jsonObject.getString("phone");
                                            id = jsonObject.getString("id");
                                            name = jsonObject.getString("name");
                                        }
                                    }
                                    if(!success.equals("1")){
                                        commonUtils.MessageDialog(message, null, new CommonUtils.MyAction() {
                                            @Override
                                            public void actionPerfomed() {
                                                passwordEdt.setText("");
                                            }

                                            @Override
                                            public void actionCancel() {

                                            }
                                        });
                                    }else{
                                        myprefs.saveUserData(id,name,phone,image);
                                        getActivity().finish();
                                        startActivity(new Intent(getActivity(),MainActivity.class));
                                    }
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
