package com.groupbsse.ourapp;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.groupbsse.ourapp.classes.MyLocation;
import com.groupbsse.ourapp.classes.ServiceList;
import com.groupbsse.ourapp.classes.Users;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
import com.groupbsse.ourapp.util.LocationTracker;
import com.groupbsse.ourapp.util.Myprefs;
import com.groupbsse.ourapp.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class sendMeetPointFragment extends Fragment implements ReciversDialog.OnComplete {


    public sendMeetPointFragment() {
        // Required empty public constructor
    }

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1,CROP_IMAGE = 2;
    private ImageView ivImage;
    private String userChoosenTask;
    Uri imageUri,imageUri2;
    ContentValues values;

    Bitmap localBitmap,uploadedbitmap;
    EditText desc,location;

    String lat,lng;

    LocationTracker locationTracker;
    boolean valueGot = false;

    ProgressDialog progressDialog;
    Connection connection;


    private FloatingActionMenu fam;
    private FloatingActionButton fab3,fab1,fab2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.set_meet_up, container, false);
        desc = (EditText)rootView.findViewById(R.id.desc);
        location = (EditText)rootView.findViewById(R.id.location);
        location.setFocusable(false);
        fab1 = (FloatingActionButton)rootView.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)rootView.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)rootView.findViewById(R.id.fab3);
        fam = (FloatingActionMenu)rootView.findViewById(R.id.fab_menu);
        ivImage = (ImageView)rootView.findViewById(R.id.imageView);

        connection = new Connection();

        locationTracker = new LocationTracker(getActivity());

        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    fab1.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    fab3.setVisibility(View.VISIBLE);
                    fam.setLayoutParams(new CoordinatorLayout.LayoutParams(com.github.clans.fab.FloatingActionMenu.LayoutParams.MATCH_PARENT, com.github.clans.fab.FloatingActionMenu.LayoutParams.MATCH_PARENT));
                } else {
                    fab1.setVisibility(View.GONE);
                    fab2.setVisibility(View.GONE);
                    fab3.setVisibility(View.GONE);
                    CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(com.github.clans.fab.FloatingActionMenu.LayoutParams.WRAP_CONTENT, com.github.clans.fab.FloatingActionMenu.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                    fam.setLayoutParams(params);
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

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                if(ServiceList.usersArrayList!= null){
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject1 = new JSONObject();
                    JSONObject jsonObject = null;
                    try {
                        for(Users us : ServiceList.usersArrayList){
                            jsonObject = new JSONObject();
                            jsonObject.put("Id",us.getUserid());
                            jsonArray.put(jsonObject);
                        }
                        jsonObject1.put("Recivers",jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Log.d("recivers",String.valueOf(jsonObject1));
                    if(ServiceList.usersArrayList.size() == 0){
                        Toast.makeText(getActivity(),"please add atleast on reciver",Toast.LENGTH_SHORT).show();
                    }else{
                        String Desc = desc.getText().toString();
                        if(Desc.trim().equals("")){
                            Toast.makeText(getActivity(),"Please fill in description",Toast.LENGTH_SHORT).show();
                        }else{
                            if(uploadedbitmap == null){
                                Toast.makeText(getActivity(),"Please add a photo",Toast.LENGTH_SHORT).show();
                            }else{
                                //Log.d("recivers",String.valueOf(jsonObject1));
                                setMeetPoint(new Myprefs(getActivity()).getUserData().get("userid"),Desc,location.getText().toString(),lat,lng,BitMapToString(uploadedbitmap),String.valueOf(jsonObject1));
                            }

                        }
                    }
                }else{
                    Toast.makeText(getActivity(),"please add atleast on reciver",Toast.LENGTH_SHORT).show();
                }


            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                ReciversDialog reciversDialog = new ReciversDialog();
                reciversDialog.show(getFragmentManager(), "dialog");
                reciversDialog.setCompleteListener(sendMeetPointFragment.this);


            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
               if(location.getText().toString().equals("")){

                    getLocationAddress();
                }


            }
        });


        super.onResume();
    }

    public void getLocationAddress(){

        if(progressDialog == null){
            progressDialog = ProgressDialog.show(sendMeetPointFragment.this.getActivity(),null,"Please wait..");
        }

        locationTracker.getPhysicalLocation(locationTracker.getLatitude(), locationTracker.getLongitude(), new LocationTracker.GetName() {
            @Override
            public void returnname(ArrayList<MyLocation> s) {
                int i = 0;
                String featurename = "";
                if(s.size() == 1){
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    lat = String.valueOf(locationTracker.getLatitude());
                    lng = String.valueOf(locationTracker.getLongitude());
                    if(s.get(0).getFeaturename().length() > 5 && !(s.get(0).getFeaturename().equals(s.get(0).getLocationname()))){
                        Log.d("jjjjjjk",s.get(0).getFeaturename());
                      featurename = s.get(0).getFeaturename();
                    }else {
                        Log.d("jjjjjjL",s.get(0).getLocationname());
                    }
                    location.setText(featurename+" "+s.get(0).getLocationname());
                }else{
                    getLocationAddress();
                }

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                try {

                    Uri picked = data.getData();
                    File file = new File(getRealPathFromURI(picked));
                    localBitmap = decodeFile(file);
                    imageUri = getImageUri(getActivity(),localBitmap);

                    if(!((Activity) getActivity()).isFinishing()){
                        performCrop(imageUri, null);
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "There was a problem", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                //onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA) {

                try {
                    File file = new File(getRealPathFromURI(imageUri));
                    localBitmap = decodeFile(file);
                    imageUri = getImageUri(getActivity(),localBitmap);
                    if(!((Activity) getActivity()).isFinishing())
                    {
                        performCrop(imageUri,localBitmap);
                    }


                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Photo",e.getMessage());
                    //Log.d("Data",data.toString());
                    e.printStackTrace();
                }
                // onCaptureImageResult(data);
            }
            else if(requestCode == CROP_IMAGE) {
                try {

                    //uploadedbitmap = (Bitmap)data.getExtras().getParcelable("data");
                    Bitmap avatar = null;
                    if (data.hasExtra("data"))
                    {
                        uploadedbitmap = data.getParcelableExtra("data");
                    }
                    else
                    {
                        uploadedbitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri2);
                    }

                    ivImage.setImageBitmap(uploadedbitmap);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Photo",e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void performCrop(Uri uri,Bitmap inImage)
    {
        try
        {
            Log.d("performingcrop", "ok to perform crop");
            Intent localIntent = new Intent("com.android.camera.action.CROP");
            localIntent.setDataAndType(uri, "image/*");
            localIntent.putExtra("crop", "true");
            localIntent.putExtra("aspectX", 3);
            localIntent.putExtra("aspectY", 3);
            localIntent.putExtra("outputX", 500);
            localIntent.putExtra("outputY", 500);
            localIntent.putExtra("return-data", true);
            if(inImage != null){
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "Title", null);
                imageUri2 = Uri.parse(path);
                localIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri2);
            }

            startActivityForResult(localIntent, 2);
            return;
        }
        catch (ActivityNotFoundException localActivityNotFoundException)
        {
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = null;

        try {
            System.gc();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            //Log.d("EWN", "Out of memory error catched");
            e.printStackTrace();

        } catch (OutOfMemoryError e) {

            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
            Log.d("EWN", "Out of memory error catched");
        }
        return temp;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private Bitmap decodeFile(File f){
        int IMAGE_MAX_SIZE = 1500;
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return b;
    }

    public void setMeetPoint(String userid,String desc,String locationname,String lat,String lng,String image,String recivers){
        progressDialog = ProgressDialog.show(sendMeetPointFragment.this.getActivity(),null,"Setting meet point...");
        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        map.put("username",new Myprefs(getActivity()).getUserData().get("username"));
        map.put("desc",desc);
        map.put("locationname",locationname);
        map.put("lat",lat);
        map.put("lng",lng);
        map.put("image",image);
        map.put("recivers",recivers);
        map.put("link",LinkUrls.address);
        connection.makeRequest(LinkUrls.sendMeetpoint, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Log.d("response",response);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null)
                            progressDialog.dismiss();

                        String success = null;
                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    success = jsonObject.getString("success");
                                }
                                if(success.equals("1")){
                                    new CommonUtils(sendMeetPointFragment.this.getActivity()).MessageDialog("Sent", null, new CommonUtils.MyAction() {
                                        @Override
                                        public void actionPerfomed() {
                                            MainActivity act= (MainActivity)getActivity();
                                            MeetUpFragment fragment= new MeetUpFragment();
                                            FragmentManager fm= act.getSupportFragmentManager();
                                            fm.beginTransaction()
                                                    .replace(R.id.content_frame, fragment, "Meet").commit();
                                        }

                                        @Override
                                        public void actionCancel() {

                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void userList(ArrayList<Users> list) {
        ServiceList.usersArrayList.clear();
        ServiceList.usersArrayList.addAll(list);
        //Toast.makeText(getActivity(),"Added "+String.valueOf(list.size())+ "Contacts",Toast.LENGTH_SHORT).show();
    }
}
