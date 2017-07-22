package com.shuvojitkar.tourist.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuvojitkar.tourist.R;

/**
 * Created by SHOBOJIT on 7/22/2017.
 */

public class Home_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);
    }
}
