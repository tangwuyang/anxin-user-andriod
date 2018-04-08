package com.anxin.kitchen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 订单切换tab fragment
 * Created by xujianjun on 2018/4/5.
 */

public class OrderTabAdapter extends FragmentPagerAdapter {
    private String[] mTabName;
    private Fragment[] mFragments;

    public OrderTabAdapter(FragmentManager fm,String[] tab, Fragment[] fragments) {
        super(fm);
        this.mTabName =tab;
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mTabName.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabName[position];
    }
}
