package com.shuvojitkar.tourist.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    public String name;
    public String image;
    public String lan;
    public String lat;
    private String descriptipn;
    Context cn;
    private ImageView mPlaceImage;
    private TextView mPlaceName;

    private GoogleMap mMap;
    private LatLng latLng;
    private TextView desc_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            init();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        lan = extras.getString("Lang");
        lat =extras.getString("Lat");
        name = extras.getString("Name");
        image =extras.getString("Image");
        descriptipn = extras.getString("Description");

        mPlaceName.setText(name);
        desc_textview.setText(descriptipn);
        Picasso.with(this).load(image).into(mPlaceImage);
        Toast.makeText(this, "Lang : "+lan, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Lat : "+lat, Toast.LENGTH_SHORT).show();

    }

    private void init() {
        mPlaceImage = (ImageView) findViewById(R.id.detail_imageView);
        mPlaceName = (TextView) findViewById(R.id.detail_place_name);
        desc_textview = (TextView)findViewById(R.id.descrition_textview);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap!= null){
            mMap = googleMap;
            latLng = new LatLng(Double.parseDouble(lan),Double.parseDouble(lat));
            Toast.makeText(getBaseContext(), "Lat: "+lat +"Lang: "+ lan , Toast.LENGTH_SHORT).show();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            mMap.getUiSettings().setScrollGesturesEnabled(false);

        }else{
            Toast.makeText(getBaseContext(),"Map not ready yet" , Toast.LENGTH_SHORT).show();
        }
    }
}
