package com.groupbsse.ourapp;

import android.app.Activity;
import android.app.Fragment;
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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.groupbsse.ourapp.adapters.MyFriendsAdapter;
import com.groupbsse.ourapp.classes.Contact;
import com.groupbsse.ourapp.classes.ServiceList;
import com.groupbsse.ourapp.classes.Users;
import com.groupbsse.ourapp.classes.Warning;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.CommonUtils;
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


public class WarningDetails extends AppCompatActivity implements AudioDialog.OnDoneClicked,ReciversDialog.OnComplete{

    static String path;

    EditText desc,audiopath;
    Button audio,image,addcontacts;
    private FloatingActionMenu fam;
    private com.github.clans.fab.FloatingActionButton fab3,fab1,fab2;
    AudioDialog newFragment;
    AlertDialog alertDialog;
    MyFriendsAdapter myFriendsAdapter;
    ArrayList<Users> selected;
    ArrayList<Users> arrayList;
    RecyclerView  recyclerView;

    Connection connection;
    CommonUtils commonUtils;
    ProgressDialog progressDialog;
    ProgressBar progressBar;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1,CROP_IMAGE = 2;
    private ImageView ivImage;
    private String userChoosenTask;
    Uri imageUri;
    ContentValues values;

    Bitmap localBitmap,uploadedbitmap;

    Myprefs myprefs;

    Bundle extras;
    String cords;
    ArrayList<Users> contactHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_details);
        desc = (EditText)findViewById(R.id.desc);
        /*audio = (Button)findViewById(R.id.btn_audio);*/
        audiopath = (EditText)findViewById(R.id.audiopath);
        image = (Button)findViewById(R.id.btn_image);
     /*   addcontacts = (Button)findViewById(R.id.addcontacts);*/
        ivImage = (ImageView)findViewById(R.id.imageView);
        /*fab = (FloatingActionButton)findViewById(R.id.fab);*/
        fab3 = (FloatingActionButton)findViewById(R.id.fab3);
        fab1 = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fab2);
        fam = (FloatingActionMenu)findViewById(R.id.fab_menu);
        newFragment = new AudioDialog();

        connection = new Connection();
        commonUtils = new CommonUtils(WarningDetails.this);
        myprefs = new Myprefs(getApplicationContext());

        extras = getIntent().getExtras();

        if(extras != null){
            cords = extras.getString("cords");
        }


        /*audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentTra ft = getFragmentManager().beginTransaction();
                // Create and show the dialog.
                newFragment.show(getSupportFragmentManager(), "dialog");
            }
        })*/;

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    selectImage();
                }catch (Exception e){
                    Log.d("Toucherror",e.getMessage());
                }

            }
        });

        /*addcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReciversDialog reciversDialog = new ReciversDialog();
                reciversDialog.show(getSupportFragmentManager(), "dialog");
                reciversDialog.setCompleteListener(WarningDetails.this);
            }
        });*/

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


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                newFragment.show(getSupportFragmentManager(), "dialog");
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
               /* ReciversDialog reciversDialog = new ReciversDialog();
                reciversDialog.show(getSupportFragmentManager(), "dialog");
                reciversDialog.setCompleteListener(WarningDetails.this);*/

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(WarningDetails.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.reciver_dialog, null);
                dialogBuilder.setView(dialogView);

                connection = new Connection();
                recyclerView = (RecyclerView)dialogView.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                progressBar = (ProgressBar)dialogView.findViewById(R.id.progress);
                TextView empty = (TextView)dialogView.findViewById(R.id.empty);
                final EditText search = (EditText) dialogView.findViewById(R.id.search);
                Button done = (Button)dialogView.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected = new ArrayList<Users>();
                        ArrayList<Users> stList = ((MyFriendsAdapter) myFriendsAdapter).getStudentist();
                        for(Users us : stList){
                            if(us.isSelected() == true){
                                selected.add(new Users(us.getUserid(),us.getImage(),us.getUsername(),us.getPhone()));
                                //Toast.makeText(ReciversDialog.this.getActivity(),us.getUsername()+" "+us.getPhone(),Toast.LENGTH_SHORT).show();
                            }

                        }

                        ServiceList.usersArrayList.clear();
                        ServiceList.usersArrayList.addAll(selected);
                        alertDialog.dismiss();

                    }
                });
                alertDialog = dialogBuilder.create();
                alertDialog.show();
                getFriendList(new Myprefs(getApplicationContext()).getUserData().get("userid"));
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                if(desc.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),"Please give the warning a description",Toast.LENGTH_SHORT).show();
                }else{
                    if(ServiceList.usersArrayList.size() == 0 || ServiceList.usersArrayList == null){
                        Toast.makeText(getApplicationContext(),"please add atleast on reciver",Toast.LENGTH_SHORT).show();
                    }else{
                        if(audiopath.getText().toString().trim().equals("")){
                            commonUtils.ConfirmationDialog("You sending warning without audio", null, new CommonUtils.MyAction() {
                                @Override
                                public void actionPerfomed() {
                                    taskSend("image",BitMapToString(uploadedbitmap),null);
                                }

                                @Override
                                public void actionCancel() {

                                }
                            });
                        }
                        if(uploadedbitmap == null){
                            commonUtils.ConfirmationDialog("You sending warning without image", null, new CommonUtils.MyAction() {
                                @Override
                                public void actionPerfomed() {
                                    File file  = new File(audiopath.getText().toString());
                                    taskSend("audio","",file);
                                }

                                @Override
                                public void actionCancel() {

                                }
                            });
                        }
                        if(audiopath.getText().toString().trim().equals("") && uploadedbitmap == null){
                            commonUtils.ConfirmationDialog("You sending warning without audio and image", null, new CommonUtils.MyAction() {
                                @Override
                                public void actionPerfomed() {
                                    taskSend("none","",null);
                                }

                                @Override
                                public void actionCancel() {

                                }
                            });
                        }

                        if(!(audiopath.getText().toString().trim().equals("") || uploadedbitmap == null)){
                            //Toast.makeText(getApplicationContext(),"Sending...",Toast.LENGTH_SHORT).show();
                            File file  = new File(audiopath.getText().toString());
                            taskSend("both",BitMapToString(uploadedbitmap),file);
                        }
                    }

                }
            }
        });

    }


    @Override
    protected void onResume() {
       // Toast.makeText(getApplicationContext(),"resume",Toast.LENGTH_SHORT).show();
        ServiceList.usersArrayList.clear();
        super.onResume();

    }

    public void taskSend(String type,String image,File file){

                if(ServiceList.usersArrayList != null){
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
                        Toast.makeText(getApplicationContext(),"please add atleast on reciver",Toast.LENGTH_SHORT).show();
                    }else{

                         sendWarning(new Myprefs(getApplicationContext()).getUserData().get("userid"),desc.getText().toString(),commonUtils.getCurrentDate(),image,type,cords,String.valueOf(jsonObject1),file);
                        Log.d("recivers",String.valueOf(jsonObject1));
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"please add atleast on reciver",Toast.LENGTH_SHORT).show();
                }
    }


    @Override
    public void GetAudioPath(String path) {
        //WarningDetails.path = path;
        audiopath.setText(path);
    }


    public void sendWarning(String userid, String desc, String date, String image , String media,String cords ,String recivers,File audio){
        progressDialog = ProgressDialog.show(WarningDetails.this,null,"Please wait..");
        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        map.put("username",new Myprefs(getApplicationContext()).getUserData().get("username"));
        map.put("image",image);
        map.put("date",date);
        map.put("desc",desc);
        map.put("media",media);
        map.put("reciver",recivers);
        map.put("cords",cords);
        map.put("link",LinkUrls.address);

        connection.makeMultipartRequest(LinkUrls.SendWarning, map, audio, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null) {
                            progressDialog.dismiss();
                            commonUtils.MessageDialog("Warning Sent", null, new CommonUtils.MyAction() {
                                @Override
                                public void actionPerfomed() {
                                    finish();
                                    Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                                    mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mainActivity.putExtra("action","warnlist");
                                    startActivity(mainActivity);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                try {

                    Uri picked = data.getData();
                    File file = new File(getRealPathFromURI(picked));
                    localBitmap = decodeFile(file);
                    imageUri = getImageUri(getApplicationContext(),localBitmap);
                    performCrop(imageUri);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                //onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA) {

                try {

                    File file = new File(getRealPathFromURI(imageUri));
                    localBitmap = decodeFile(file);
                    imageUri = getImageUri(getApplicationContext(),localBitmap);
                    performCrop(imageUri);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                // onCaptureImageResult(data);
            }
            else if(requestCode == CROP_IMAGE) {
                try {
                    uploadedbitmap = (Bitmap)data.getExtras().getParcelable("data");
                    ivImage.setImageBitmap(uploadedbitmap);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(WarningDetails.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(WarningDetails.this);

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
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void performCrop(Uri uri)
    {
        try
        {
            Log.d("performingcrop", "ok to perform crop");
            Intent localIntent = new Intent("com.android.camera.action.CROP");
            localIntent.setDataAndType(uri, "image/*");
            localIntent.putExtra("crop", "true");
            localIntent.putExtra("aspectX", 3);
            localIntent.putExtra("aspectY", 3);
            localIntent.putExtra("outputX", 1500);
            localIntent.putExtra("outputY", 1500);
            localIntent.putExtra("return-data", true);
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
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
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


    public void getFriendList(String userid){
        if(arrayList == null)
            arrayList = new ArrayList<>();
        else if(arrayList.size() > 0)
            arrayList.clear();

        HashMap<String ,String> map = new HashMap<>();
        map.put("userid",userid);
        map.put("command","friends");
        connection.makeRequest(LinkUrls.getFriendsList, map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        String name,phone,userid;
                        if(!response.equals("error")){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(jsonObject.getString("success").equals("1")){
                                        name = jsonObject.getString("name");
                                        phone = jsonObject.getString("number");
                                        userid = jsonObject.getString("userid");
                                        arrayList.add(new Users(userid,"",name,phone));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(arrayList.size() > 0){
                                myFriendsAdapter = new MyFriendsAdapter(getApplicationContext(),arrayList);
                                recyclerView.setAdapter(myFriendsAdapter);
                                //empty.setVisibility(View.GONE);
                            }else{
                                //empty.setVisibility(View.VISIBLE);
                            }


                        }

                    }
                });
            }
        });
    }

    @Override
    public void userList(ArrayList<Users> list) {
        /*usersArrayList = new ArrayList<>();
        usersArrayList.addAll(list);*/
        ServiceList.usersArrayList.clear();
        ServiceList.usersArrayList.addAll(list);
    }
}
