package com.groupbsse.ourapp;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AlertDetails extends AppCompatActivity {

    MapView mapView;
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    TextView name,description,location;
    ProgressDialog progressDialog;

    double lat,lng;
    String title,username,message,locname;
    Bundle extras;

    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        name = (TextView)findViewById(R.id.name);
        description = (TextView)findViewById(R.id.desc);
        location = (TextView)findViewById(R.id.location);
        connection = new Connection();
        extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getString("alertmessageid") != null){
                getDetailsRemote(extras.getString("alertmessageid"));
            }else{
                username = extras.getString("username");
                message = extras.getString("desc");
                lat = extras.getDouble("lat");
                lng = extras.getDouble("lng");
                locname = extras.getString("location");
                title = "Sender :"+username+" \n"+" Message :"+message;
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        LatLng sydney = new LatLng(lat, lng);
                        googleMap.addMarker(new MarkerOptions().position(sydney)
                                .title(title));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
                        // Zoom in, animating the camera.
                        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                    }
                });
                name.setText(username);
                description.setText(message);
                location.setText(locname);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cancel, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_done){
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    public void getDetailsRemote(String messageid){
        progressDialog = ProgressDialog.show(AlertDetails.this,null,"Loading wait");
        HashMap<String ,String> map = new HashMap<>();
        map.put("messageid",messageid);
        connection.makeRequest(LinkUrls.getSingleAlert, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null)
                            progressDialog.dismiss();

                        if(!response.equals("error")){

                            String sender = "",desc ="",locationres = "";
                            double lat = 0,lng = 0;
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i <jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    desc = jsonObject.getString("desc");
                                    locationres = jsonObject.getString("location");
                                    lat = jsonObject.getDouble("lat");
                                    lng = jsonObject.getDouble("lng");
                                    sender = jsonObject.getString("name");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            final double finalLat = lat;
                            final double finalLng = lng;
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    mMap = googleMap;
                                    LatLng sydney = new LatLng(finalLat, finalLng);
                                    googleMap.addMarker(new MarkerOptions().position(sydney)
                                            .title(title));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
                                    // Zoom in, animating the camera.
                                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                }
                            });

                            name.setText(sender);
                            description.setText(desc);
                            location.setText(locationres);
                        }


                    }
                });
            }
        });
    }
}
