package com.groupbsse.ourapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.groupbsse.ourapp.MyAlerts;
import com.groupbsse.ourapp.SendAlert;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class AlertPagerAdapter  extends FragmentPagerAdapter {
    int tabCount;
    public AlertPagerAdapter(FragmentManager fm , int noTabs) {
        super(fm);
        this.tabCount = noTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                SendAlert sendAlert = new SendAlert();
                return sendAlert;
            case 1:
                MyAlerts myAlerts = new MyAlerts();
                return myAlerts;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
