package com.example.easystock.views.adapters;

import android.content.Context;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class CheckoutViewPagerAdapter extends FragmentStatePagerAdapter {
    //private List<String> fragmentTitleList;
    private List<Fragment> fragmentList;

    public CheckoutViewPagerAdapter(@NonNull FragmentManager fm, int behaviour, List<Fragment> fragmentList) {
        super(fm, behaviour);
        this.fragmentList = fragmentList;
        //this.fragmentTitleList = fragmentTitle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    


/*    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }*/

/*    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }*/
}


