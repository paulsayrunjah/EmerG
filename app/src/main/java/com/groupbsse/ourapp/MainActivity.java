package com.groupbsse.ourapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.groupbsse.ourapp.classes.FriendsOnline;
import com.groupbsse.ourapp.classes.NotificationClass;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.LocationTracker;
import com.groupbsse.ourapp.util.Myprefs;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Button nav_button;
    ImageView imageView;
    TextView username;

    NavigationView navigationView;
    ProgressDialog progressDialog;
    Myprefs myprefs;
    Bundle extras;

    int exitCounter;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    int finishCount = 0;

    CommonUtils commonUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myprefs = new Myprefs(getApplicationContext());
        myprefs.saveValueRunning(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        imageView = (ImageView)hView.findViewById(R.id.profile_image);
        nav_button = (Button) hView.findViewById(R.id.btn_nav_header);
        username = (TextView)hView.findViewById(R.id.username);

        extras = getIntent().getExtras();
        commonUtils = new CommonUtils(getApplicationContext());
        new doInBackground().execute();

        if(extras != null){
            String action = extras.getString("action");
            if(action != null && action.equals("notify")){
                Log.d("Flagssss","oooooooooooooo");
                Bundle bundle = new Bundle();
                bundle.putInt("position",extras.getInt("position"));
                Notifications notifications = new Notifications();
                notifications.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, notifications, "").commit();
            }else if(action != null && action.equals("warnlist")){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new WarnFragment(), "").commit();
            }else if(action != null && action.equals("friend")){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FriendsFragment(), "").commit();
            }else if(action != null && action.equals("trackme")){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new TracksFragment(), "").commit();
            }

        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new AlertsFragment(), "").commit();
        }
    }


    @Override
    protected void onResume() {
        Menu nav_Menu = navigationView.getMenu();
        if(new Myprefs(getApplicationContext()).getUserData().get("userid") == null){
            nav_button.setText("Login");
           // Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.warn).setEnabled(false);
            nav_Menu.findItem(R.id.meetup).setEnabled(false);
            nav_Menu.findItem(R.id.profile).setEnabled(false);
            nav_Menu.findItem(R.id.notifications).setEnabled(false);
            nav_Menu.findItem(R.id.add_friends).setEnabled(false);
            username.setText("Guest account");

        }else {
            nav_button.setText("Logout");
            username.setText(new Myprefs(getApplicationContext()).getUserData().get("username"));

        }


        nav_Menu.findItem(R.id.meetup).setVisible(false);
        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new Myprefs(getApplicationContext()).getUserData().get("userid") == null){
                    startActivity(new Intent(MainActivity.this,LoSiActivity.class));
                }else{
                    Log.d("Clicked :","True");
                    progressDialog = ProgressDialog.show(MainActivity.this,null,"Logging out....");
                    logout(new Myprefs(getApplicationContext()).getUserData().get("userid"));
                }
            }
        });




        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        super.onResume();
    }

    @Override
    protected void onStart() {
        myprefs.saveValueRunning(true);
        //isRunningForeGround = true;
        super.onStart();
    }

    @Override
    protected void onPause() {
        myprefs.saveValueRunning(false);
        // isRunningForeGround = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        myprefs.saveValueRunning(false);
        //isRunningForeGround = false;
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(exitCounter < 1){
            exitCounter++;
            Toast.makeText(this,"Press again to exit", Toast.LENGTH_SHORT).show();
            try {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exitCounter = 0;
                    }
                },2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            myprefs.saveValueRunning(true);
            finish();
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.alert) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new AlertsFragment(), "").commit();
        } else if (id == R.id.warn) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new WarnFragment(), "").commit();
        } else if (id == R.id.meetup) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new MeetUpFragment(), "").commit();
        } else if (id == R.id.contacts) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new MyContacts(), "").commit();

        } else if (id == R.id.profile) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new UserProfile() , "").commit();
        } else if (id == R.id.nav_send) {
            Intent feedbackEmail = new Intent(Intent.ACTION_SEND);
            feedbackEmail.setType("text/email");
            feedbackEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {"wetaaseteam@gmail.com"});
            feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Feed back");
            startActivity(Intent.createChooser(feedbackEmail, "Send Via"));
        }else if(id == R.id.add_friends){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FriendsFragment(), "").commit();
        }else if(id == R.id.notifications){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new Notifications(), "").commit();
        }else if(id == R.id.trackme){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new TracksFragment(), "").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(String userid){
        Connection connection = new Connection();
        final CommonUtils commonUtils = new CommonUtils(MainActivity.this);
        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        connection.makeRequest(LinkUrls.loginOff, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("response",response);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(progressDialog != null)
                            progressDialog.dismiss();


                        if(!response.equals("error")){
                             String success = null;
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i =0; i < jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    success = jsonObject.getString("success");
                                }

                                if(success.equals("1")){
                                    new Myprefs(getApplicationContext()).clearUserData();
                                    startActivity(new Intent(MainActivity.this,LoSiActivity.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            commonUtils.MessageDialog("check connection and try again", null, new CommonUtils.MyAction() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        Toast.makeText(MainActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        finish();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void GetOnLineContacts(){
        Connection connection = new Connection();
        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",new Myprefs(getApplicationContext()).getUserData().get("userid"));
        map.put("command","friends");
        connection.makeRequest(LinkUrls.getFriendsList, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("Response",response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String status,name,number,fid,userid,image;
                        String success;
                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(jsonObject.getString("success").equals("1")){
                                        name = jsonObject.getString("name");
                                        number = commonUtils.formartMobileNumber(jsonObject.getString("number"));
                                        List<FriendsOnline> contactlist = FriendsOnline.find(FriendsOnline.class, "myid = ? and number = ?",new Myprefs(getApplicationContext()).getUserData().get("userid"), number);

                                        if(contactlist.size() < 1){
                                            FriendsOnline friendsOnline = new FriendsOnline(new Myprefs(getApplicationContext()).getUserData().get("userid"),name,number);
                                            friendsOnline.save();
                                        }

                                    }else{

                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{

                        }

                    }
                });

            }
        });
    }

    private class doInBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if(myprefs.getUserData().get("userid") != null){
                GetOnLineContacts();
            }
            return null;
        }
    }
}
