package com.groupbsse.ourapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.groupbsse.ourapp.MyAlerts;
import com.groupbsse.ourapp.SendAlert;
import com.groupbsse.ourapp.TrackMeFragment;
import com.groupbsse.ourapp.TrackedUsersFragment;

/**
 * Created by Sayrunjah on 10/02/2017.
 */
public class TrackPagerAdapter extends FragmentPagerAdapter {
    int tabCount;
    public TrackPagerAdapter(FragmentManager fm , int noTabs) {
        super(fm);
        this.tabCount = noTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                TrackedUsersFragment trackedUsersFragment = new TrackedUsersFragment();
                return trackedUsersFragment;
            case 1:
                TrackMeFragment trackMeFragment = new TrackMeFragment();
                return trackMeFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
