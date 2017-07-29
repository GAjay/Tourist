package com.shuvojitkar.tourist.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shuvojitkar.tourist.Fragment.Profile_Fragment.User_Post_fragment;
import com.shuvojitkar.tourist.Fragment.Profile_Fragment.User_friends_fragment;
import com.shuvojitkar.tourist.Fragment.Profile_Fragment.User_profile_fragment;

/**
 * Created by SHOBOJIT on 7/6/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                User_profile_fragment userProfile = new User_profile_fragment();
                return userProfile;
            case 1:
                User_friends_fragment uf = new User_friends_fragment();
                return uf;
            case 2:
                User_Post_fragment userPost = new User_Post_fragment();
                return userPost;

         default:
             return  null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Profile";
            case 1:
                return "Friends";
            case 2 :
                return "News Feed";
            default:
                return  null;
        }
    }
}
