package com.groupbsse.ourapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.groupbsse.ourapp.classes.ServiceList;
import com.groupbsse.ourapp.classes.Contact;
import com.groupbsse.ourapp.adapters.ContactAdapter;
import com.groupbsse.ourapp.util.ContactsLoader;
import com.groupbsse.ourapp.util.RecyclerTouchListener;


import java.util.ArrayList;
import java.util.List;

public class ContactsPickerActivity extends AppCompatActivity implements ActionMode.Callback{

    RecyclerView contactsChooser;
    LinearLayoutManager layout_manager;
    //Button btnDone;
    EditText txtFilter;
    TextView txtLoadInfo;
    ContactAdapter contactsListAdapter;
    ContactsLoader contactsLoader;
    ActionMode actionMode;
   // ArrayList<Contact> contactslist;

    CheckBox select_all;
     String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_picker);

        contactsChooser = (RecyclerView) findViewById(R.id.lst_contacts_chooser);
        //btnDone = (Button) findViewById(R.id.btn_done);
        txtFilter = (EditText) findViewById(R.id.txt_filter);
        txtLoadInfo = (TextView) findViewById(R.id.txt_load_progress);
        select_all = (CheckBox) findViewById(R.id.select_all);
       type = "many";



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //The key argument here must match that used in the other activity
        }


        layout_manager = new LinearLayoutManager(ContactsPickerActivity.this);
        contactsChooser.setLayoutManager(layout_manager);


        if(ServiceList.my_contacts.isEmpty()){
            contactsListAdapter = new ContactAdapter(ContactsPickerActivity.this, ServiceList.my_contacts);
            contactsChooser.setAdapter(contactsListAdapter);
            loadContacts("");
        }else{
            txtLoadInfo.setVisibility(View.GONE);
            contactsListAdapter = new ContactAdapter(ContactsPickerActivity.this, ServiceList.my_contacts);
            contactsChooser.setAdapter(contactsListAdapter);
            contactsListAdapter.updateSelected();
            actionMode = startSupportActionMode(ContactsPickerActivity.this);
            String title = getString(R.string.selected_count, contactsListAdapter.getSelectedItemCount()+"");
            actionMode.setTitle(title);

        }


        txtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactsListAdapter.filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        select_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    if (actionMode != null) {
                        AllSelection();
                        return;
                    }else{
                        // Start the CAB using the ActionMode.Callback defined above
                        actionMode = startSupportActionMode(ContactsPickerActivity.this);
                        AllSelection();
                    }
                }
                else{
                    AllDeselection();
                }

            }
        });

        contactsChooser.addOnItemTouchListener(new RecyclerTouchListener(ContactsPickerActivity.this,
                contactsChooser, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                select_all.setChecked(false);
                if (actionMode != null) {
                    myToggleSelection(position);
                    Log.d("Contact :","Clicked"+String.valueOf(position));
                    return;
                }else{
                    // Start the CAB using the ActionMode.Callback defined above
                    actionMode = startSupportActionMode(ContactsPickerActivity.this);
                    myToggleSelection(position);
                    Log.d("Contact :","Clicked"+String.valueOf(position));
                }

            }


            @Override
            public void onLongClick(View view, int position) {
                if (actionMode != null) {
                    return;
                }
                // Start the CAB using the ActionMode.Callback defined above
                actionMode = startSupportActionMode(ContactsPickerActivity.this);
                myToggleSelection(position);

            }
        }));
       /* txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFilter.setFocusable(true);
            }
        });*/
        /*txtFilter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });*/

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
            ServiceList.ClearLists();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public ArrayList<Contact> getAllSelectedItems(List<Integer> list){
        ArrayList<Contact> selectedL= new ArrayList<>();

        for(int i=0; i<list.size(); i++){
            selectedL.add(ServiceList.my_contacts.get(list.get(i)));
        }

        return selectedL;
    }

    private void loadContacts(String filter){

        if(contactsLoader!=null && contactsLoader.getStatus()!= AsyncTask.Status.FINISHED){
            try{
                contactsLoader.cancel(true);
            }catch (Exception e){

            }
        }
        if(filter==null) filter="";

        try{
            //Running AsyncLoader with adapter and  filter
            contactsLoader = new ContactsLoader(this,contactsListAdapter);
            contactsLoader.txtProgress = txtLoadInfo;
            contactsLoader.execute(filter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //implementation of actionmode
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        mode.getMenuInflater().inflate(R.menu.menu_cab_recyclerviewdemoactivity, menu);
        //btnDone.setVisibility(View.GONE);
        txtFilter.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_done:
                if(contactsListAdapter.getSelectedItemCount()==0){
                    setResult(RESULT_CANCELED);
                    actionMode.finish();
                    finish();
                }
                else{

                    Intent resultIntent = new Intent();

                    resultIntent.putParcelableArrayListExtra("SelectedContacts",
                            getAllSelectedItems(contactsListAdapter.getSelectedItems()));
                    setResult(RESULT_OK,resultIntent);
                    actionMode.finish();
                    finish();
                  /*  if(type.equals("one") && contactsListAdapter.getSelectedItemCount()>1){

                        Toast.makeText(getApplicationContext(),"Select one contact",Toast.LENGTH_LONG).show();

                    }else{


                    }*/

                }

                return true;

            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.actionMode = null;
        contactsListAdapter.clearSelections();
        //btnDone.setVisibility(View.VISIBLE);
        txtFilter.setVisibility(View.VISIBLE);
        select_all.setChecked(false);

    }
    //end of actionmode implementation

    private void myToggleSelection(int idx) {
        contactsListAdapter.toggleSelection(idx);
        String title = getString(R.string.selected_count, contactsListAdapter.getSelectedItemCount()+"");
        actionMode.setTitle(title);

        if(type.equals("one")){
            Intent resultIntent = new Intent();

            resultIntent.putParcelableArrayListExtra("SelectedContacts",
                    getAllSelectedItems(contactsListAdapter.getSelectedItems()));
            setResult(RESULT_OK,resultIntent);
            actionMode.finish();
            finish();
        }

    }

    private void AllSelection() {
        contactsListAdapter.selectAll();
        String title = getString(R.string.selected_count, contactsListAdapter.getSelectedItemCount()+"");
        actionMode.setTitle(title);
    }

    private void AllDeselection() {
        contactsListAdapter.clearSelections();
        String title = getString(R.string.selected_count, contactsListAdapter.getSelectedItemCount()+"");
        actionMode.setTitle(title);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
