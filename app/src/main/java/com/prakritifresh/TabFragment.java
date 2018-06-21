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

public class TabFragment extends Fragment {
    public static int int_items = 3;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    String TabFragmentB;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    class C01971 implements Runnable {
        C01971() {
        }

        public void run() {
            TabFragment.tabLayout.setupWithViewPager(TabFragment.viewPager);
        }
    }

    class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TabFragmentVeg();
                case 1:
                    return new GridFragmentFruit();
                case 2:
                    return new GridFragmentGrain();
                default:
                    return null;
            }
        }

        public int getCount() {
            return TabFragment.int_items;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Vegetable";
                case 1:
                    return "Fruits";
                case 2:
                    return "Staples";
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
        View x = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.post(new C01971());
        return x;
    }
}
