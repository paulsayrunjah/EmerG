package com.groupbsse.ourapp;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groupbsse.ourapp.adapters.AlertPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlertsFragment extends Fragment {

    TabLayout tabLayout;
   public static ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_alerts, container, false);
        tabLayout = (TabLayout)rootView.findViewById(R.id.tab_layout);

        //Initializing viewPager
        viewPager = (ViewPager)rootView.findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("Alert"));
        tabLayout.addTab(tabLayout.newTab().setText("My Alerts"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        AlertPagerAdapter alertPagerAdapter = new AlertPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(alertPagerAdapter);

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
