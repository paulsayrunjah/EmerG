package com.groupbsse.ourapp.DataAdapters;

import com.groupbsse.ourapp.connection.Connection;
import com.groupbsse.ourapp.connection.LinkUrls;

import java.util.HashMap;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class UserDataAdapter {

    String response;
    Connection conn = new Connection();
    public void login(HashMap<String ,String> params, final ServerResponse serverResponse){
       conn.makeRequest(LinkUrls.loginUser, params, new Connection.GetResponse() {
           @Override
           public void response(String response) {
               serverResponse.response(response);
           }
       });
    }

    public void register(HashMap<String,String> params, final ServerResponse serverResponse){
        conn.makeRequest(LinkUrls.loginUser, params, new Connection.GetResponse() {
            @Override
            public void response(String response) {
                serverResponse.response(response);
            }
        });
    }

    public interface ServerResponse{
        void response(String response);
    }
}
