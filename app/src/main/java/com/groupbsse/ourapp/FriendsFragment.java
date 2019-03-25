package com.groupbsse.ourapp;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groupbsse.ourapp.adapters.AlertPagerAdapter;
import com.groupbsse.ourapp.adapters.FriendPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {


    public FriendsFragment() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    public static ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        tabLayout = (TabLayout)rootView.findViewById(R.id.tab_layout);

        //Initializing viewPager
        viewPager = (ViewPager)rootView.findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Requests"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        FriendPagerAdapter friendPagerAdapter = new FriendPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(friendPagerAdapter);

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
        return rootView;
    }

}
