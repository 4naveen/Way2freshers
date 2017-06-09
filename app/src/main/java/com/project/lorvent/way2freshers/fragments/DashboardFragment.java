package com.project.lorvent.way2freshers.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.project.lorvent.way2freshers.R;
import com.project.lorvent.way2freshers.utils.NetworkStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    Adapter adapter;
    TabLayout tabLayout;
    public DashboardFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_dashboard, container, false);
        if (!NetworkStatus.isConnected(getActivity())) {
            getActivity().finish();
        }
        setHasOptionsMenu(true);
        ActionBar actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Dashboard");
        }
        tabLayout = (TabLayout)v.findViewById(R.id.tab_layout);
        final ViewPager viewPager = (ViewPager)v.findViewById(R.id.pager);

        setupViewPager(viewPager);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        new MaterialDialog.Builder(getActivity())
                                .content("Are you sure? you want to exit!")

                                .positiveText("Ok")
                                .positiveColorRes(R.color.colorPrimary)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        getActivity().finish();

                                    }
                                })
                                .negativeColorRes(R.color.colorPrimary)
                                .negativeText("Cancel")
                                .show();


                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new RecentFragment(), "Recent Article ");
        adapter.addFragment(new NotificationFragment(), "Notification");
        adapter.addFragment(new ResultFragment(), "Results");
        viewPager.setAdapter(adapter);
    }
    public static  class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments.get(position);
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {


            return mFragmentTitles.get(position);
        }
    }
}
