package com.groupbsse.ourapp.util;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import com.groupbsse.ourapp.adapters.ContactAdapter;
import com.groupbsse.ourapp.classes.Contact;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Sayrunjah on 09/02/2017.
 */
public class ContactsLoader extends AsyncTask<String,Void,Void> {

    ContactAdapter contactsListAdapter;
    Context context;
    private ArrayList<Contact> tempContactHolder;
    public TextView txtProgress;
    int totalContactsCount,loadedContactsCount;


    public  ContactsLoader(Context context,ContactAdapter contactsListAdapter){
        this.context = context;
        this.contactsListAdapter = contactsListAdapter;
        this.tempContactHolder= new ArrayList<>();
        loadedContactsCount=0;
        totalContactsCount=0;
        txtProgress=null;
    }

    @Override
    protected Void doInBackground(String[] filters) {


        String filter = filters[0];

        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        Cursor cursor;
        if(filter.length()>0) {
            cursor = contentResolver.query(
                    uri,
                    projection,
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?",
                    new String[]{filter},
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            );
        }else {

            cursor = contentResolver.query(
                    uri,
                    projection,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            );

        }
        totalContactsCount = cursor.getCount();
        if(cursor!=null && cursor.getCount()>0){



            while(cursor.moveToNext()) {
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{id},
                            null
                    );

                    if (phoneCursor != null && phoneCursor.getCount() > 0) {

                        while (phoneCursor.moveToNext()) {
                            String phId = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

                            String customLabel = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));

                            String label = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(),
                                    phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)),
                                    customLabel
                            );

                            String phNo = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                            tempContactHolder.add(new Contact(phId, name, phNo, label));



                        }
                        phoneCursor.close();

                    }

                }
                loadedContactsCount++;

                publishProgress();


            }
            cursor.close();

            //remove duplicates
            Set<Contact> set =  new TreeSet<Contact>(new Comparator() {
                @Override
                public int compare(Object lhs, Object rhs) {
                    if(((Contact)lhs).getNumber().equalsIgnoreCase(((Contact)rhs).getNumber())
                            || ((Contact)lhs).getNumber().replace(" ","").equalsIgnoreCase(((Contact)rhs).getNumber().replace(" ",""))
                            || ((Contact)lhs).getName().equalsIgnoreCase(((Contact)rhs).getName())){
                        return 0;
                    }
                    return 1;
                }


            });
            set.addAll(tempContactHolder);
            ArrayList<Contact> newarraylist= new ArrayList<>(set);

            tempContactHolder.clear();
            tempContactHolder.addAll(newarraylist);

            //end of remove duplicates

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void[] v){




        if(this.tempContactHolder.size()>=1) {


            //contactsListAdapter.addContacts(tempContactHolder);

            //this.tempContactHolder.clear();

            if(txtProgress!=null){
                txtProgress.setVisibility(View.VISIBLE);
                String progressMessage = "Loading...("+loadedContactsCount+"/"+totalContactsCount+")";
                txtProgress.setText(progressMessage);
            }

        }

    }

    @Override
    protected void onPostExecute(Void v){

        contactsListAdapter.addContacts(tempContactHolder);
        //tempContactHolder.clear();

        if(txtProgress!=null) {
            txtProgress.setText("");
            txtProgress.setVisibility(View.GONE);
        }
    }
}
