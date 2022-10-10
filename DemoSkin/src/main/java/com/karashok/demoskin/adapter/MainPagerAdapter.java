package com.karashok.demoskin.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-18-2022
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<String> mTitles;
    private List<Fragment> mFragments;


    public MainPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments,
                            List<String> titles) {
        super(fragmentManager);
        mFragments = fragments;
        mTitles = titles;
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
        return mTitles.get(position);
    }
}
