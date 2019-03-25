package com.groupbsse.ourapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.groupbsse.ourapp.adapters.MyFriendsAdapter;
import com.groupbsse.ourapp.classes.Users;
import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;
import com.groupbsse.ourapp.util.Myprefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sayrunjah on 06/03/2017.
 */
public class ReciversDialog extends DialogFragment {


    RecyclerView recyclerView;
    MyFriendsAdapter myFriendsAdapter;
    ArrayList<Users> arrayList;
    Button done;
    TextView empty;
    EditText search;
    ProgressBar progressBar;
    Connection connection;
    ArrayList<Users> selected;

    OnComplete oncomplete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reciver_dialog, container, false);
        getDialog().setTitle("My Contact List");
        connection = new Connection();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReciversDialog.this.getActivity()));
        progressBar = (ProgressBar)v.findViewById(R.id.progress);
        empty = (TextView)v.findViewById(R.id.empty);
        search = (EditText) v.findViewById(R.id.search);
        done = (Button)v.findViewById(R.id.done);
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
                if(oncomplete != null){
                    oncomplete.userList(selected);
                }else{

                }

                dismiss();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(search.getText().toString());
                //Toast.makeText(getActivity(),search.getText().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
         getFriendList(new Myprefs(getActivity()).getUserData().get("userid"));
        return  v;
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
                                myFriendsAdapter = new MyFriendsAdapter(ReciversDialog.this.getActivity(),arrayList);
                                recyclerView.setAdapter(myFriendsAdapter);
                                empty.setVisibility(View.GONE);
                            }else{
                                empty.setVisibility(View.VISIBLE);
                            }


                        }

                    }
                });
            }
        });
    }

    public  void setCompleteListener(OnComplete oncomplete){
        this.oncomplete = oncomplete;
    }

    public interface OnComplete{
        void userList(ArrayList<Users> list);
    }

    private void performSearch(String query){
        ArrayList<Users> usersArrayList = new ArrayList<>();
        for(int i =0; i < arrayList.size(); i++){
            String userid = arrayList.get(i).getUserid();
            String name = arrayList.get(i).getUsername();
            String phone = arrayList.get(i).getPhone();

            if(name.toLowerCase().contains(query)){
                usersArrayList.add(new Users(userid,"",name,phone));
            }else if(phone.toLowerCase().contains(query)){
                usersArrayList.add(new Users(userid,"",name,phone));
            }
        }
        myFriendsAdapter = new MyFriendsAdapter(ReciversDialog.this.getActivity(),usersArrayList);
        recyclerView.setAdapter(myFriendsAdapter);
        myFriendsAdapter.notifyDataSetChanged();

        /*if(usersArrayList.size() > 0){
            myFriendsAdapter = new MyFriendsAdapter(ReciversDialog.this.getActivity(),usersArrayList);
            recyclerView.setAdapter(myFriendsAdapter);
            myFriendsAdapter.notifyDataSetChanged();
            empty.setVisibility(View.GONE);
        }else{
            empty.setVisibility(View.VISIBLE);
            empty.setText("No results found");
        }*/

    }
}
