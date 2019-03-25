package com.groupbsse.ourapp.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.groupbsse.ourapp.MeetUpFragment;
import com.groupbsse.ourapp.MyAlerts;
import com.groupbsse.ourapp.MyFriendList;
import com.groupbsse.ourapp.WarnFragment;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class AllnotificationsAdapter extends FragmentPagerAdapter {
    int tabCount;
    public AllnotificationsAdapter(FragmentManager fm , int noTabs) {
        super(fm);
        this.tabCount = noTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle extras = new Bundle();
        extras.putBoolean("notification",true);
        switch (position){
            case 0:
                MyAlerts myAlerts = new MyAlerts();
                myAlerts.setArguments(extras);
                return  myAlerts;
            case 1:
                WarnFragment warnFragment = new WarnFragment();
                warnFragment.setArguments(extras);
                return warnFragment;
            case 2:
                MeetUpFragment meetUpFragment = new MeetUpFragment();
                meetUpFragment.setArguments(extras);
                return meetUpFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
