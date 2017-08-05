package com.shuvojitkar.tourist.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuvojitkar.tourist.R;

/**
 * Created by Acid Rain on 8/5/2017.
 */

public class Search_Place_Fragment extends Fragment {
    private static View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.search_place_fragment,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
