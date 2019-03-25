package com.groupbsse.ourapp;


import android.app.NotificationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groupbsse.ourapp.adapters.AllnotificationsAdapter;
import com.groupbsse.ourapp.util.Myprefs;


/**
 * A simple {@link Fragment} subclass.
 */
public class Notifications extends Fragment {


    public Notifications() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    public static ViewPager viewPager;
    Myprefs myprefs;
    NotificationManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_notifications, container, false);
        tabLayout = (TabLayout)rootview.findViewById(R.id.tab_layout);

        //Initializing viewPager
        viewPager = (ViewPager)rootview.findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("Alerts"));
        tabLayout.addTab(tabLayout.newTab().setText("Warnings"));
        tabLayout.addTab(tabLayout.newTab().setText("Meet Ups"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
       // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        myprefs = new Myprefs(getActivity());
        myprefs.clearNotificationInfo();
        manager = (NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        manager.cancel(FirebaseMessagingService.notifyID);


        AllnotificationsAdapter allnotificationsAdapter = new AllnotificationsAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(allnotificationsAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
               /* switch (tab.getPosition()){
                    case 0:
                        break;
                    case 1:
                        break;
                }*/


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

        });
        return rootview;
    }

}
