package com.shuvojitkar.tourist.Fragment.Profile_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuvojitkar.tourist.R;

/**
 * Created by SHOBOJIT on 7/28/2017.
 */

public class User_profile_fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_user_profile_fragment,container,false);
    }
}
