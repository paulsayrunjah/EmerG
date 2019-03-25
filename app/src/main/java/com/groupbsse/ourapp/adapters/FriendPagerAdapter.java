package com.groupbsse.ourapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.groupbsse.ourapp.App_users;
import com.groupbsse.ourapp.FriendRequests;
import com.groupbsse.ourapp.MyFriendList;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class FriendPagerAdapter extends FragmentPagerAdapter {
    int tabCount;
    public FriendPagerAdapter(FragmentManager fm , int noTabs) {
        super(fm);
        this.tabCount = noTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                MyFriendList myFriendList = new MyFriendList();
                return myFriendList;
            case 1:
                FriendRequests friendRequests = new FriendRequests();
                return friendRequests;
            case 2:
                App_users app_users = new App_users();
                return app_users;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
