package com.shuvojitkar.tourist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shuvojitkar.tourist.R;

public class UserProfileSettings extends AppCompatActivity {
    String UserId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);
        UserId = getIntent().getStringExtra("Userid");
    }
}
