package com.prakritifresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragmentVeg extends Fragment {
    public static int int_items = 2;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    String TabFragmentB;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    class C01981 implements Runnable {
        C01981() {
        }

        public void run() {
            TabFragmentVeg.tabLayout.setupWithViewPager(TabFragmentVeg.viewPager);
        }
    }

    class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new GridFragment();
                case 1:
                    return new GridFragmentLeafy();
                default:
                    return null;
            }
        }

        public int getCount() {
            return TabFragmentVeg.int_items;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Regular";
                case 1:
                    return "Leafy";
                default:
                    return null;
            }
        }
    }

    public void setTabFragmentB(String t) {
        TabFragmentB = t;
    }

    public String getTabFragmentB() {
        return TabFragmentB;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.tab_layout_veg, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabsn);
        viewPager = (ViewPager) x.findViewById(R.id.viewpagern);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.post(new C01981());
        return x;
    }
}
