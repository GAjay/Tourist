package com.shuvojitkar.tourist.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shuvojitkar.tourist.Fragment.Profile_Fragment.User_Post_fragment;
import com.shuvojitkar.tourist.Fragment.Profile_Fragment.User_friends_fragment;
import com.shuvojitkar.tourist.Fragment.Profile_Fragment.User_profile_fragment;
import com.shuvojitkar.tourist.R;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView mbottomNavigationView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init ();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeFragment(0);


        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                switch (item.getItemId()){
                    case R.id. promenu_profile:
                        changeFragment(0);
                        break;
                    case R.id.promenu_friends :
                        changeFragment(1);
                        break;
                    case R.id.promenu_post :
                        changeFragment(2);
                        break;

                }
                return true;
            }
        });
    }

    private void init() {
            mToolbar = (Toolbar) findViewById(R.id.profile_appBar);
            mbottomNavigationView = (BottomNavigationView) findViewById(R.id.profile_bottom_bar);

    }


    private void changeFragment(int position) {
        Fragment selectedFragment = null;
        if (position == 0) {
            selectedFragment = new User_profile_fragment();
        } else if (position == 1) {
            selectedFragment = new User_friends_fragment();
        } else {
            selectedFragment = new User_Post_fragment();
        }

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, selectedFragment);
        transaction.commit();
    }
}
