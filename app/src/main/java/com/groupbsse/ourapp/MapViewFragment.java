package com.groupbsse.ourapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.groupbsse.ourapp.classes.MyLocation;
import com.groupbsse.ourapp.util.LocationTracker;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewFragment extends Fragment {


    public MapViewFragment() {
        // Required empty public constructor
    }

    MapView mapView;
   static GoogleMap myMap;
    double latitude,longtitude;
    String title,address;
    List<LatLng> val;
    Boolean Is_MAP_Moveable = false;

    private boolean screenLeave = false;
    int source = 0;
    int destination = 1;

    FrameLayout frameLayout;
    TextView locationame,tvdest,dest,dist;
    ImageView send;
    Bundle extras;

    LocationTracker locationTracker;
    String physicaladdress, returnedAdd;
    boolean valueGot = false;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.map_layout_view, container, false);
        mapView = (MapView)rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        locationame = (TextView)rootView.findViewById(R.id.location);
        tvdest = (TextView)rootView.findViewById(R.id.tvdest);
        dest = (TextView)rootView.findViewById(R.id.dest);
        dist = (TextView)rootView.findViewById(R.id.dist);
        send = (ImageView)rootView.findViewById(R.id.send);

        frameLayout = (FrameLayout)rootView.findViewById(R.id.fram_map);
        locationTracker = new LocationTracker(getActivity());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapViewFragment.myMap .getUiSettings().setScrollGesturesEnabled(false);
                if (Is_MAP_Moveable != true) {
                    Is_MAP_Moveable = true;
                } else {
                    Is_MAP_Moveable = false;
                }
            }
        });



        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
               Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();
                if (Is_MAP_Moveable) {
                    float x = event.getX();
                    float y = event.getY();

                    int x_co = Math.round(x);
                    int y_co = Math.round(y);

//                Projection projection = googleMap.getProjection();
                    Point x_y_points = new Point(x_co, y_co);

                    LatLng latLng = MapViewFragment.myMap.getProjection().fromScreenLocation(x_y_points);
                   Double latitude = latLng.latitude;

                    Double longitude = latLng.longitude;
                    /*HomeBean bean = new HomeBean();
                    bean.setPost_lat(String.valueOf(latitude));
                    bean.setPost_long(String.valueOf(longitude));
                    bean.setSource_id("3");
                    mLatLongList.add(bean);*/


                    System.out.println("LatLng : " + latitude + " : " + longitude);

                    LatLng point = new LatLng(latitude, longitude);
                    val.add(point);
                    int eventaction = event.getAction();
                    switch (eventaction) {
                        case MotionEvent.ACTION_DOWN:
                            // finger touches the screen
                            screenLeave = false;
//                            System.out.println("ACTION_DOWN");

//                            val.add(new LatLng(latitude, longitude));

                        case MotionEvent.ACTION_MOVE:
                            // finger moves on the screen
//                            System.out.println("ACTION_MOVE");
                          /*  if (val.size()==3){
                                val.remove(1);
                            }*/

                            val.add(new LatLng(latitude, longitude));
                            screenLeave = false;
                            Draw_Map();

                        case MotionEvent.ACTION_UP:

//                            System.out.println("ACTION_UP");
                            if (!screenLeave) {
                                screenLeave = true;
                            } else {
                                System.out.println("ACTION_UP ELSE CAse");
                                Is_MAP_Moveable = false; // to detect map is movable
                               // btn_draw_State.setImageResource(R.mipmap.erase_icon);
                                source = 0;
                                destination = 1;
                                draw_final_polygon();

                            }

                            // finger leaves the screen
//                            Is_MAP_Moveable = false; // to detect map is movable
//                            Draw_Map();
                            break;
                        default:
                            break;
                    }

                    if (Is_MAP_Moveable) {
                       // Log.e("DRAW on MAP : ", "LatLng ArrayList Size : " + mLatLongList.size());
                        return true;

                    } else {
                        return false;
                    }


                } else {
                    return false;
                }


            }
        });

        return rootView;
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onResume() {
        if(val == null){
            val = new ArrayList<>();
        }
        /*extras = getArguments();
        if(extras != null){
            if(extras.getString("cmd").equals("meetup")){
                latitude = extras.getDouble("lat");
                longtitude = extras.getDouble("lng");
                address = extras.getString("address");
                tvdest.setVisibility(View.GONE);
                dest.setVisibility(View.GONE);
                dist.setVisibility(View.GONE);
            }

        }
        locationame.setText(address);*/

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                    getLocationAddress();


            }
        });

        mapView.onResume();

        super.onResume();
    }

    public void Draw_Map() {


//        specify latitude and longitude of both source and destination Polyline

        if (val.size() > 1) {
            MapViewFragment.myMap.addPolyline(new PolylineOptions().add(val.get(source), val.get(destination)).width(8).color(ContextCompat.getColor(getActivity(), R.color.blue)));
            source++;
            destination++;
        }


    }


    private void draw_final_polygon() {

        val.add(val.get(0));

        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(val);
        polygonOptions.strokeColor(ContextCompat.getColor(getActivity(), R.color.white));
        polygonOptions.strokeWidth(8);
        polygonOptions.fillColor(ContextCompat.getColor(getActivity(), R.color.blue));
        Polygon polygon = MapViewFragment.myMap.addPolygon(polygonOptions);
    }

    public void getLocationAddress() {
        progressDialog = ProgressDialog.show(MapViewFragment.this.getActivity(), null, "Please wait..");
        locationTracker.getPhysicalLocation(locationTracker.getLatitude(), locationTracker.getLongitude(), new LocationTracker.GetName() {
            @Override
            public void returnname(ArrayList<MyLocation> s) {
                if (s.size() == 1) {
                    if (mapView != null) {

                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                MapViewFragment.myMap = googleMap;
                                LatLng sydney = new LatLng(locationTracker.getLatitude(), locationTracker.getLongitude());
                                googleMap.addMarker(new MarkerOptions().position(sydney)
                                        .title(title));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                                // Zoom in, animating the camera.
                                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                   /* googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            Intent mapDirection = new Intent(VoucherClicked.this,MapDirectionActivity.class);
                            mapDirection.putExtra("name",item_name.getText().toString());
                            mapDirection.putExtra("desc",description.getText().toString());
                            mapDirection.putExtra("lat",latitude);
                            mapDirection.putExtra("longt",longtitude);
                            startActivity(mapDirection);
                        }
                    });*/
                            }
                        });

                    }
                    locationame.setText(s.get(0).getLocationname());
                }
            }
        });
    }

}
