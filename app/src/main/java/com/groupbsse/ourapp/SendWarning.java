package com.groupbsse.ourapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.groupbsse.ourapp.util.LocationTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendWarning extends Fragment {


    public SendWarning() {
        // Required empty public constructor
    }

    private GoogleMap mMap;
    public static boolean mMapIsTouched = false;
    Projection projection;
    public double latitude;
    public double longitude;

    Boolean Is_MAP_Moveable = false;

    ArrayList<LatLng> val;
    PolygonOptions rectOptions;
    Polygon polygon;
    LocationTracker locationTracker;

    private FloatingActionMenu fam;
    private FloatingActionButton fab3,fab1,fab2;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_send_warning, container, false);
        FrameLayout fram_map = (FrameLayout) rootView.findViewById(R.id.fram_map);
       /* Button clear = (Button) rootView.findViewById(R.id.clear);
        Button send = (Button)rootView.findViewById(R.id.send);*/

        fab1 = (FloatingActionButton)rootView.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)rootView.findViewById(R.id.fab2);
        fam = (FloatingActionMenu)rootView.findViewById(R.id.fab_menu);


        locationTracker = new LocationTracker(getActivity());

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
               // mMap.getUiSettings().setAllGesturesEnabled(false);
                LatLng sydney = new LatLng(locationTracker.getLatitude(), locationTracker.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(sydney)
                        .title("Me"));
               googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,18));
                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
            }
        });

        val = new ArrayList<>();
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
                mMap.clear();
                val.clear();
                LatLng myposition = new LatLng(locationTracker.getLatitude(), locationTracker.getLongitude());
                mMap.addMarker(new MarkerOptions().position(myposition)
                        .title("Me"));

            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                JSONArray jsonArray = new JSONArray() ;
                JSONObject jsonObject1 = new JSONObject();


                if(val.size() > 0) {
                    try {
                        for (int j = 0; j < val.size(); j++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("Lat", val.get(j).latitude);
                            jsonObject.put("Lng", val.get(j).longitude);
                            jsonArray.put(jsonObject);
                        }
                        jsonObject1.put("Cords", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent myIntent = new Intent(getActivity(), WarningDetails.class);
                    myIntent.putExtra("cords", String.valueOf(jsonObject1));
                    startActivity(myIntent);
                }else{
                    Toast.makeText(getActivity(),"Please draw polygon around the affected  area",Toast.LENGTH_SHORT).show();
                }

            }
        });
        fram_map.setOnTouchListener(new View.OnTouchListener() {     @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            int x_co = Math.round(x);
            int y_co = Math.round(y);

            projection = mMap.getProjection();
            Point x_y_points = new Point(x_co, y_co);

            LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
            latitude = latLng.latitude;

            longitude = latLng.longitude;

            int eventaction = event.getAction();
            switch (eventaction) {
                case MotionEvent.ACTION_DOWN:
                    // finger touches the screen
                    val.add(new LatLng(latitude, longitude));

                case MotionEvent.ACTION_MOVE:
                    // finger moves on the screen
                    val.add(new LatLng(latitude, longitude));

                case MotionEvent.ACTION_UP:
                    // finger leaves the screen
                    Draw_Map();
                    break;
            }

            /*if (Is_MAP_Moveable == true) {
                return true;

            } else {
                return false;
            }*/
            return true;
        }
        });

        return rootView;
    }

    public void Draw_Map() {
        rectOptions = new PolygonOptions();
        rectOptions.addAll(val);
        rectOptions.strokeColor(Color.BLUE);
        rectOptions.strokeWidth(7);
        rectOptions.fillColor(Color.CYAN);
        polygon = mMap.addPolygon(rectOptions);
    }

    public void removeDrawn(){

        polygon.remove();
    }



}
