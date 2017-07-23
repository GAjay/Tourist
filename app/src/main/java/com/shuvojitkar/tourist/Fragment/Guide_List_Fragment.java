package com.shuvojitkar.tourist.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuvojitkar.tourist.R;

/**
 * Created by SHOBOJIT on 7/22/2017.
 */

public class Guide_List_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guide_fragment,container,false);
    }
}
