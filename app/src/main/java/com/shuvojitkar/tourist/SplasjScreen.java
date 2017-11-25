package com.shuvojitkar.tourist;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplasjScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splasj_screen);
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.up_to_down);
        TextView spLogo = (TextView) findViewById(R.id.txt);
        spLogo.setAnimation(anim);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplasjScreen.this,MainActivity.class));
                finish();
            }
        },2000);
    }
}
