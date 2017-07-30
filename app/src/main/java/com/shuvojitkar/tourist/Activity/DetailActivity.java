package com.shuvojitkar.tourist.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnNearByFound {

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

    private int PROXIMITY_RADIUS = 300;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;

    private Double longitude, latitude;

    private ArrayList<PlaceDetails> nearbyPlacesList;
    private int loop;

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
                execute("hospital");
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

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {

            if (haveNetworkConnection() == true) {
                String url = getUrl(latitude, longitude, type);
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

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 0) {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null) {

                //Start searching for location and update the location text when update available.
// Do whatever you want
                showToast("Gps enabled", Toast.LENGTH_LONG);
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
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
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
    public void placeFound(ArrayList<PlaceDetails> nearbyPlacesList2) {


        if (nearbyPlacesList != null) {
            this.nearbyPlacesList = nearbyPlacesList2;
            for (loop = 0; loop < nearbyPlacesList.size(); loop++) {


                MarkerOptions markerOptions = new MarkerOptions();
                PlaceDetails googlePlace = nearbyPlacesList.get(loop);


                new AsyncTask<PlaceDetails, Void, String>() {

                    @Override
                    protected String doInBackground(PlaceDetails... params) {

                        String data = "";
                        InputStream iStream = null;
                        HttpURLConnection urlConnection = null;

                        PlaceDetails placeDetails = (PlaceDetails) params[0];


                        String lat= placeDetails.getLat();
                        String lan = placeDetails.getLan();

                        try {
                            URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lan+"&sensor=true");

                            urlConnection = (HttpURLConnection) url.openConnection();

                            urlConnection.connect();

                            // Reading data from url
                            iStream = urlConnection.getInputStream();

                            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                            StringBuffer sb = new StringBuffer();

                            String line = "";
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }

                            data = sb.toString();
                            Log.d("downloadUrl", data.toString());
                            br.close();

                        } catch (Exception e) {
                            Log.d("Exception", e.toString());
                        } finally {
                            try {
                                iStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            urlConnection.disconnect();
                        }


                        return data;
                    }

                    @Override
                    protected void onPostExecute(String result) {

                        JSONObject object1 = null;
                        try {
                            JSONObject object = new JSONObject(result);

                            JSONArray array1 = (JSONArray) object.get("results");

                            object1 = array1.getJSONObject(0);

                            JSONArray array2 = (JSONArray) object1.get("address_components");
                            JSONObject name1 = (JSONObject) array2.get(0);
                            String long_name = String.valueOf(name1.get("long_name"));
                            Toast.makeText(getBaseContext(), long_name, Toast.LENGTH_LONG).show();



                        } catch (JSONException e) {
                            Toast.makeText(getBaseContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
                        }

                    }
                }.execute(googlePlace);

                double lat = Double.parseDouble(googlePlace.getLat());
                double lng = Double.parseDouble(googlePlace.getLan());

                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                mMap.addMarker(markerOptions);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        }
    }
}
