package com.shuvojitkar.tourist.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.shuvojitkar.tourist.DialogBoxes.NearByPlaceDialog;
import com.shuvojitkar.tourist.Map_Helpers.GetNearbyPlacesData;
import com.shuvojitkar.tourist.Map_Helpers.OnNearByFound;
import com.shuvojitkar.tourist.Map_Helpers.PlaceDetails;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnNearByFound {

    public String place_name;
    public String place_image;
    private String descriptipn;
    private ImageView mPlaceImage;
    private TextView mPlaceName;

    private GoogleMap mMap;
    private LatLng latLng;
    private TextView desc_textview;

    private Button m_zoomin, m_zoomout;
    private int zoom_amount = 15;
    private CircleImageView findHospital, findResturent, findpolice;

    private int PROXIMITY_RADIUS = 15000;

    private GoogleApiClient mGoogleApiClient;

    private Double longitude, latitude;

    private NearByPlaceDialog nearByPlaceDialog;
    private String searchType = "notset";

    private AlertDialog dialog = null;


    private boolean onexecuting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

        }



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        longitude = Double.parseDouble(extras.getString("Lang"));
        latitude = Double.parseDouble(extras.getString("Lat"));
        place_name = extras.getString("Name");
        place_image = extras.getString("Image");
        descriptipn = extras.getString("Description");
        mPlaceName.setText(place_name);
        desc_textview.setText(descriptipn);
        Picasso.with(this).load(place_image).into(mPlaceImage);

    }

    private void init() {
        mPlaceImage = (ImageView) findViewById(R.id.detail_imageView);
        mPlaceName = (TextView) findViewById(R.id.detail_place_name);
        desc_textview = (TextView) findViewById(R.id.descrition_textview);
        m_zoomin = (Button) findViewById(R.id.map_zoomin);
        m_zoomout = (Button) findViewById(R.id.map_zoomout);
        findHospital = (CircleImageView) findViewById(R.id.hsp_button);
        findResturent = (CircleImageView) findViewById(R.id.res_button);
        findpolice = (CircleImageView) findViewById(R.id.police_button);

        m_zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (zoom_amount < 160) {
                    zoom_amount += 2;
                    set_map_zoom(zoom_amount);
                } else {
                    showToast("At Max zoom leve", Toast.LENGTH_SHORT);
                }
            }
        });

        m_zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (zoom_amount > 2) {
                    zoom_amount -= 2;

                    set_map_zoom(zoom_amount);
                } else {
                    showToast("At Min zoom leve", Toast.LENGTH_SHORT);
                }
            }
        });

        findHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                execute("hospital");
            }
        });
        findpolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execute("police");
            }
        });
        findResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execute("restaurant");
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
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(place_name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(300);
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

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + longitude + "," + latitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyD-2lbqP9aonaHTxg3r_8L4PgzRx0SEdZ8");

        Log.d("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();

    }


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


    private void execute(String type) {

        //Todo

        if (searchType.equals(type) && dialog != null) {
            dialog.show();
            Toast.makeText(this, "Showing prev dialog", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!onexecuting) {
            onexecuting = !onexecuting;
            searchType = type;
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {

                if (haveNetworkConnection() == true) {

                    String url = getUrl(latitude, longitude, searchType);
                    Object[] DataTransfer = new Object[4];
                    DataTransfer[0] = mMap;
                    DataTransfer[1] = url;
                    DataTransfer[2] = getBaseContext();
                    DataTransfer[3] = this;
                    new GetNearbyPlacesData().execute(DataTransfer);
                } else {
                    Snackbar.make(findViewById(R.id.home_nav), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        } else {
            showToast("You Have another process running\nPlease wait till its done", Toast.LENGTH_LONG);
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 0) {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null) {
                if (haveNetworkConnection() == true) {

                    String url = getUrl(latitude, longitude, searchType);
                    Object[] DataTransfer = new Object[4];
                    DataTransfer[0] = mMap;
                    DataTransfer[1] = url;
                    DataTransfer[2] = getBaseContext();
                    DataTransfer[3] = this;
                    new GetNearbyPlacesData().execute(DataTransfer);
                } else {
                    Snackbar.make(findViewById(R.id.home_nav), "No Internet Connection", Snackbar.LENGTH_LONG)
                            .show();
                }
            } else {
                //Users did not switch on the GPS
            }
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void placeFound(ArrayList<PlaceDetails> nearbyPlacesList) {
        nearByPlaceDialog = new NearByPlaceDialog(DetailActivity.this, nearbyPlacesList, latitude, longitude, place_name);
        dialog = nearByPlaceDialog.getDialog();
        dialog.show();
        onexecuting = !onexecuting;
    }
}
