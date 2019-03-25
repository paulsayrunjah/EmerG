package com.groupbsse.ourapp.util;

import android.location.Address;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sayrunjah on 02/07/2017.
 */
public abstract class LocationFromWeb extends AsyncTask<String , String, String> {

    public static JSONObject getLocationInfo(final String address) {

        StringBuilder stringBuilder = new StringBuilder();
        try {

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?latlng=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        return jsonObject;
    }
    public static String getAddrByWeb(JSONObject jsonObject){
        List<Address> res = new ArrayList<Address>();
        String name = "";
        try
        {
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++)
            {
                Double lon = new Double(0);
                Double lat = new Double(0);

                try
                {
                    lon = array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    lat = array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    name = array.getJSONObject(i).getString("formatted_address");
                    Address addr = new Address(Locale.getDefault());
                    addr.setLatitude(lat);
                    addr.setLongitude(lon);
                    addr.setAddressLine(0, name != null ? name : "");
                    res.add(addr);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();

                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }

        return name;
    }

    @Override
    protected String doInBackground(String... params) {
        String address = params[0];
        StringBuilder stringBuilder = new StringBuilder();
        try {

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?latlng=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<Address> res = new ArrayList<Address>();
        String name = "";
        try
        {
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++)
            {
                Double lon = new Double(0);
                Double lat = new Double(0);

                try
                {
                    lon = array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    lat = array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    name = array.getJSONObject(i).getString("formatted_address");
                    Address addr = new Address(Locale.getDefault());
                    addr.setLatitude(lat);
                    addr.setLongitude(lon);
                    addr.setAddressLine(0, name != null ? name : "");
                    res.add(addr);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();

                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }
        return name;
    }


    protected abstract void onPostExecute(String s);
}
