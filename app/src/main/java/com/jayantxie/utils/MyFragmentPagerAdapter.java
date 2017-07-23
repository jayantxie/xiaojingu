package com.jayantxie.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by 天亮就出发 on 2017/3/18.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> list;
    public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> list){
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0){
        return list.get(arg0);
    }
}
