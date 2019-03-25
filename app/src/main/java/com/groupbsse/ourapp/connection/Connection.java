package com.groupbsse.ourapp.connection;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Sayrunjah on 09/02/2017.
 */
public class Connection {

    public void makeRequest(String url, HashMap<String ,String> map, final GetResponse getResponse){

        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();

        for(Map.Entry<String, String> entry : map.entrySet()){
            formBuilder.add(entry.getKey(),entry.getValue());
        }
        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e){
                //connectionResponse = e.toString();
                getResponse.response("error");
                //connectionresp = "nooooooo";
                Log.d("ConnectionError", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //connectionResponse = response.body().string();
                getResponse.response(response.body().string());

            }
        });

    }

    public void  makeMultipartRequest(String url, HashMap<File,String> filemap, HashMap<String,String> map, final GetResponse getResponse) {
        MultipartBody.Builder multiBody = new MultipartBody.Builder();
        multiBody.setType(MultipartBody.FORM);
        int count = 0;
        for (Map.Entry<File, String> entry : filemap.entrySet()) {
            multiBody.addFormDataPart("file"+String.valueOf(count++), entry.getKey().getName(), RequestBody.create(MediaType.parse(entry.getValue()), entry.getKey()));
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            multiBody.addFormDataPart(entry.getKey(), entry.getValue());
        }

        RequestBody requestBody = multiBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                getResponse.response("error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getResponse.response(response.body().string());

            }

        });
    }


    public void makeMultipartRequest(String url,HashMap<String ,String> map,File audio,final GetResponse getResponse){
        MultipartBody.Builder multiBody = new MultipartBody.Builder();
        multiBody.setType(MultipartBody.FORM);
        if(audio != null){
            multiBody.addFormDataPart("audiofile",audio.getName(), RequestBody.create(MediaType.parse("audio/*"), audio));
        }


        for (Map.Entry<String, String> entry : map.entrySet()) {
            multiBody.addFormDataPart(entry.getKey(), entry.getValue());
        }

        RequestBody requestBody = multiBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                getResponse.response("error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getResponse.response(response.body().string());

            }

        });


    }

    public interface GetResponse{
        void response(String response);
    }

}
