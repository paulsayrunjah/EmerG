package com.groupbsse.ourapp;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.groupbsse.ourapp.classes.CustomMessage;
import com.groupbsse.ourapp.util.Myprefs;

import java.io.File;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide titlebar of application
        // must be before setting the layout
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide statusbar of Android
        // could also be done later
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

    }


    @Override
    protected void onResume() {
        createDirectories();
        Myprefs myprefs = new Myprefs(getApplicationContext());
        if(myprefs.isInit() == false){
            CustomMessage customMessage = new CustomMessage("Property is being stolen","0","2");
            CustomMessage customMessage1 = new CustomMessage("I have got an accident","0","1");
            CustomMessage customMessage2 = new CustomMessage("My house has caught fire","0","3");
            CustomMessage customMessage3 = new CustomMessage("Am lost can find my way home","0","2");
            customMessage.save();
            customMessage1.save();
            customMessage2.save();
            customMessage3.save();
            myprefs.init(true);
        }
        getToken();
        super.onResume();
    }

    protected void getToken(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Myprefs myprefs = new Myprefs(getApplicationContext());
                if(myprefs.getToken() == null){
                    String token = FirebaseInstanceId.getInstance().getToken();
                    if(token == null){
                        //Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        getToken();
                    }else{
                        myprefs.storeToken(token);
                        startActivity(new Intent(Splash.this,LoSiActivity.class));
                        finish();
                    }
                }else{
                    startActivity(new Intent(Splash.this,LoSiActivity.class));
                    finish();
                }

            }
        });

    }

    private void createDirectories(){
        //Create Folder
        File folder = new File(Environment.getExternalStorageDirectory().toString()+"/wetase/audioclips");
        if (!folder.exists()) {
            folder.mkdirs();
        }

    }


}
