package com.pet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by dragon on 2018/6/12.
 * 我的界面的viewPager的适配器
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private int PAGE_COUNT = 2;
    private String[] tableTitle ;//= new String[]{"关注", "动态"};
    private List<Fragment> mFragmentTab;

    public MyFragmentPagerAdapter(FragmentManager fm, String[] tableTitle,List<Fragment> mFragmentTab,int PAGE_COUNT) {
        super(fm);
        this.mFragmentTab = mFragmentTab;
        this.tableTitle = tableTitle;
        this.PAGE_COUNT = PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentTab.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tableTitle[position];
    }

}

