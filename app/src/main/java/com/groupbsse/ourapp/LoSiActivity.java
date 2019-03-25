package com.groupbsse.ourapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.groupbsse.ourapp.util.Myprefs;

public class LoSiActivity extends AppCompatActivity {

    int exitCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lo_si);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO); // Customize if you need to
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);//ButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Login");

        /*Myprefs myprefs = new Myprefs(getApplicationContext());
        if(myprefs.getToken() == null){
            try{
                String token = FirebaseInstanceId.getInstance().getToken();
                myprefs.storeToken(token);
            }catch (Exception e){
            }

        }*/
        if(new Myprefs(getApplicationContext()).getUserData().get("userid") == null){
            FragmentManager fm= getSupportFragmentManager();
            loadFragment(new LoginFragment());
        }else{
            startActivity(new Intent(LoSiActivity.this,MainActivity.class));
            finish();
        }

    }

    public void loadFragment(final Fragment fragment) {

        // create a transaction for transition here
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        // put the fragment in place
        transaction.replace(R.id.frame_container, fragment);

        // this is the part that will cause a fragment to be added to backstack,
        // this way we can return to it at any time using this tag
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
