package com.groupbsse.ourapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.groupbsse.ourapp.classes.ServiceList;
import com.groupbsse.ourapp.classes.Warning;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.groupbsse.ourapp.util.LocationTracker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WarnDetails extends AppCompatActivity {

    GoogleMap mMap;

    ArrayList<LatLng> val;
    PolygonOptions rectOptions;
    Polygon polygon;
    SupportMapFragment mapFragment;

    Bundle bundle;
    String desc,image,audio,arrayCords;
    Double lat,lng;
    ArrayList<LatLng> cords;

    CardView cardView;
    ImageView btn_audio,btn_image;
    CommonUtils commonUtils;
    int position;

    TextView description;
    MediaPlayer mPlayer;

    Connection connection;
    EditText audiostring,imagestring;
    Long localdbid;
    ProgressDialog progressDialog;
    LocationTracker locationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        cardView = (CardView)findViewById(R.id.media);
        btn_audio = (ImageView)findViewById(R.id.btn_audio);
        btn_image = (ImageView)findViewById(R.id.btn_image);
        description = (TextView)findViewById(R.id.desc);
        audiostring = (EditText)findViewById(R.id.audiostring);
        imagestring = (EditText)findViewById(R.id.imagestring);

        commonUtils = new CommonUtils(WarnDetails.this);
        connection = new Connection();

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImage(imagestring.getText().toString());
            }
        });

        btn_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(audiostring.getText().toString());
            }
        });
        bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("warningid") != null){
                getWarning(bundle.getString("warningid"));
                if(bundle.getLong("localdbid",0) != 0){
                    localdbid = bundle.getLong("localdbid");
                }
            }
        }

        locationTracker = new LocationTracker(getApplicationContext());

    }

    @Override
    protected void onResume() {
        progressDialog = ProgressDialog.show(WarnDetails.this, null, "Please wait....");
        if(val == null)
            val = new ArrayList<>();
        else if(val.size() > 0)
            val.clear();



        if(bundle != null){
            if(bundle.getString("warningid") != null){
            }else {
                image = bundle.getString("image");
                audio = bundle.getString("audio");
                desc = bundle.getString("desc");
                //arrayCords = bundle.getString("cords");
                position = bundle.getInt("position");

                if(audio.equals("no") && image.equals("no")){
                    cardView.setVisibility(View.GONE);
                }else if(audio.equals("no")){
                    btn_audio.setVisibility(View.GONE);
                }else if(image.equals("no")){
                    btn_image.setVisibility(View.GONE);
                }
                if(!image.equals("no")){
                    imagestring.setText(image);
                }
                if(!audio.equals("no")){
                    audiostring.setText(audio);
                }
                final LocationTracker locationTracker = new LocationTracker(getApplicationContext());
                description.setText(desc);
                val.addAll(ServiceList.cords.get(position));
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        if(progressDialog != null)
                            progressDialog.dismiss();

                        mMap = googleMap;
                        Draw_Map(val);
                        // mMap.getUiSettings().setAllGesturesEnabled(false);
                        LatLng sydney = new LatLng(ServiceList.cords.get(position).get(0).latitude, ServiceList.cords.get(position).get(0).longitude);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
                        // Zoom in, animating the camera.
                        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

                    }
                });
            }

        }


        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cancel, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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

    public void Draw_Map(ArrayList<LatLng> val) {
        rectOptions = new PolygonOptions();
        rectOptions.addAll(val);
        rectOptions.strokeColor(Color.BLUE);
        rectOptions.strokeWidth(7);
        rectOptions.fillColor(Color.CYAN);
        polygon = mMap.addPolygon(rectOptions);
    }

    public void playAudio(String audio){
        //Toast.makeText(getApplicationContext(),audio,Toast.LENGTH_SHORT).show();
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(audio);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        if(!mPlayer.isPlaying()){
            mPlayer.start();
        }else{
            mPlayer.pause();
        }

    }

    public void viewImage(String image){
        final Dialog dialog = new Dialog(WarnDetails.this);
        dialog.setTitle("Image");
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_image_view);
        ImageView imageView = (ImageView)dialog.findViewById(R.id.imageView);
        final ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        if (android.os.Build.VERSION.SDK_INT >= 10) {
            try {
                Glide.with(WarnDetails.this)
                        .load(image)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception arg0, String arg1,
                                                       Target<GlideDrawable> arg2, boolean arg3) {
                                //localImageView.setImageResource(R.drawable.ic_empty);
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable arg0, String arg1,
                                                           Target<GlideDrawable> arg2, boolean arg3, boolean arg4) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })//.crossFade()
                        //.centerCrop()
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Uri localUri = Uri.parse(image);
            Picasso.with(WarnDetails.this).load(localUri).into(imageView, new Callback() {
                @Override
                public void onError() {
                    //imageView.setImageResource(R.drawable.ic_empty);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
        dialog.show();
    }

    public void getWarning(String warningid){
        HashMap<String ,String> map = new HashMap<>();
        map.put("warningid",warningid);
        map.put("link",LinkUrls.address);
        connection.makeRequest(LinkUrls.getSingleWarning, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("response",response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                        String id = "",desc = "",image = "",audio = "",time = "";
                        ArrayList<LatLng> cords = null;
                        if(!response.equals("error")){

                            try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i =0; i <jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONArray arrayC = jsonObject.getJSONArray("cords");

                                cords = new ArrayList<LatLng>();
                                id = jsonObject.getString("id");
                                desc = jsonObject.getString("desc");
                                image = jsonObject.getString("image");
                                audio = jsonObject.getString("audio");
                                time = jsonObject.getString("sent_at");

                                for(int c=0; c < arrayC.length();c++){
                                    JSONObject jsonObject1 = arrayC.getJSONObject(c);
                                    Double lat = jsonObject1.getDouble("lat");
                                    Double lng = jsonObject1.getDouble("lng");
                                    cords.add(new LatLng(lat,lng));
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                            if(localdbid != 0){
                                Warning warning = Warning.findById(Warning.class,localdbid);
                                warning.seen = true;
                                warning.save();
                                Log.d("localid",String.valueOf(localdbid));

                            }

                            if(audio.equals("no") && image.equals("no")){
                                cardView.setVisibility(View.GONE);
                            }
                            if(audio.equals("no")){
                                btn_audio.setVisibility(View.GONE);
                            }
                            if(image.equals("no")){
                                btn_image.setVisibility(View.GONE);
                            }
                            if(!image.equals("no")){
                                imagestring.setText(image);
                            }
                            if(!audio.equals("no")){
                                audiostring.setText(audio);
                            }

                            description.setText(desc);
                            val.addAll(cords);
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    mMap = googleMap;
                                    Draw_Map(val);
                                    // mMap.getUiSettings().setAllGesturesEnabled(false);
                                    LatLng sydney = new LatLng(val.get(0).latitude, val.get(0).longitude);
                /*googleMap.addMarker(new MarkerOptions().position(sydney)
                        .title("Me"));*/
                                    LatLng me = new LatLng(locationTracker.getLatitude(), locationTracker.getLongitude());
                                    googleMap.addMarker(new MarkerOptions().position(me)
                                            .title("Me"));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
                                    // Zoom in, animating the camera.
                                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

                                }
                            });
                        }
                    }
                });
            }
        });

    }
}
