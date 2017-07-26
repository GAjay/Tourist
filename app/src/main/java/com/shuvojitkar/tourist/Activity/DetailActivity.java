package com.shuvojitkar.tourist.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    public String name;
    public String image;
    public String lan;
    public String lat;
    Context cn;
    private ImageView mPlaceImage;
    private TextView mPlaceName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        lan = extras.getString("Lang");
        lat =extras.getString("Lat");
        name = extras.getString("Name");
        image =extras.getString("Image");

        mPlaceName.setText(name);
        Picasso.with(this).load(image).into(mPlaceImage);

            Toast.makeText(this, "Lang : "+lan, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Lat : "+lat, Toast.LENGTH_SHORT).show();



    }

    private void init() {
        mPlaceImage = (ImageView) findViewById(R.id.detail_imageView);
        mPlaceName = (TextView) findViewById(R.id.detail_place_name);
    }
}
