package com.shuvojitkar.tourist.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shuvojitkar.tourist.Map_Helpers.GetNearbyPlacesData;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public String name;
    public String image;
    public String lan;
    public String lat;
    private String descriptipn;
    private ImageView mPlaceImage;
    private TextView mPlaceName;

    private GoogleMap mMap;
    private LatLng latLng;
    private TextView desc_textview;

    private Button m_zoomin, m_zoomout, findHospital, findResturent;
    private int zoom_amount = 15;

    private int PROXIMITY_RADIUS = 10000;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;

    private Double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //Check For Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            init();
            buildGoogleApiClient();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        longitude = Double.parseDouble(extras.getString("Lang"));
        latitude = Double.parseDouble(extras.getString("Lat"));
        name = extras.getString("Name");
        image = extras.getString("Image");
        descriptipn = extras.getString("Description");

        mPlaceName.setText(name);
        desc_textview.setText(descriptipn);
        Picasso.with(this).load(image).into(mPlaceImage);
    }

    private void init() {
        mPlaceImage = (ImageView) findViewById(R.id.detail_imageView);
        mPlaceName = (TextView) findViewById(R.id.detail_place_name);
        desc_textview = (TextView) findViewById(R.id.descrition_textview);
        m_zoomin = (Button) findViewById(R.id.map_zoomin);
        m_zoomout = (Button) findViewById(R.id.map_zoomout);
        findHospital = (Button) findViewById(R.id.hsp_button);

        m_zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zoom_amount < 160) {
                    zoom_amount += 5;
                    set_map_zoom(zoom_amount);
                } else {
                    showToast("At Max zoom leve", Toast.LENGTH_SHORT);
                }
            }
        });

        m_zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zoom_amount > 5) {
                    zoom_amount -= 5;
                    set_map_zoom(zoom_amount);
                } else {
                    showToast("At Min zoom leve", Toast.LENGTH_SHORT);
                }
            }
        });

        findHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getUrl(latitude, longitude, "hospital");
                Object[] DataTransfer = new Object[3];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                DataTransfer[2] = getBaseContext();
                new GetNearbyPlacesData().execute(DataTransfer);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            latLng = new LatLng(longitude, latitude);
            mMap = googleMap;
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_amount));
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(300);
            circleOptions.strokeColor(Color.BLACK);
            circleOptions.fillColor(0x30ff0000);
            circleOptions.strokeWidth(10);
            mMap.addCircle(circleOptions);
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);

            mMap.getUiSettings().setCompassEnabled(true);

        } else {
            showToast("Map is not ready yet", Toast.LENGTH_SHORT);
        }
    }


    private void set_map_zoom(int zoom_amount) {

        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_amount));
        } else
            showToast("Map is not ready yet", Toast.LENGTH_SHORT);
    }

    private void showToast(String massage, int duration) {
        Toast.makeText(getBaseContext(), massage, duration).show();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {


        // 24.8955847,91.864673


        double latitude1 = 24.8955847, longitude1 = 91.864673;


        return "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=" + latitude1 + "," + longitude1 + "&radius=" + PROXIMITY_RADIUS + "&type=" + nearbyPlace + "&key=AIzaSyD-2lbqP9aonaHTxg3r_8L4PgzRx0SEdZ8";


    }

                
                //vvv

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
