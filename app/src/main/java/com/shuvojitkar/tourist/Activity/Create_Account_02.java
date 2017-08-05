package com.shuvojitkar.tourist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shuvojitkar.tourist.R;

public class Create_Account_02 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign02);


        findViewById(R.id.touristlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(Create_Account_02.this,Tourist_Create_Account.class));
            }
        });

        findViewById(R.id.guidelogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Create_Account_02.this,Guide_Create_Account.class));
            }
        });
    }
}
