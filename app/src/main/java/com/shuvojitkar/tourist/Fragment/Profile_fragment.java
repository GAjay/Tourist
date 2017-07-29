package com.shuvojitkar.tourist.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.shuvojitkar.tourist.Adapter.SectionsPagerAdapter;
import com.shuvojitkar.tourist.R;

/**
 * Created by SHOBOJIT on 7/28/2017.
 */

public class Profile_fragment extends Fragment {
    private TabLayout tb;
    private ViewPager vp;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_fragment,container,false);
        init(v);
        return v;
    }

    private void init(View v) {
        vp  = (ViewPager) v.findViewById(R.id.profile_tabpager);
        tb = (TabLayout) v.findViewById(R.id.profile_tabs);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());


        //set up the viewpager
        vp.setAdapter(mSectionsPagerAdapter);
        tb.setupWithViewPager(vp);

    }


}
