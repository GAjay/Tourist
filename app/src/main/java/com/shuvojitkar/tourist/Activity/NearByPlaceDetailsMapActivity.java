package com.shuvojitkar.tourist.Activity;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.shuvojitkar.tourist.Map_Helpers.Distance;
import com.shuvojitkar.tourist.Map_Helpers.Duration;
import com.shuvojitkar.tourist.Map_Helpers.FindRoutsBetween;
import com.shuvojitkar.tourist.Map_Helpers.OnRouteFound;
import com.shuvojitkar.tourist.Map_Helpers.Route;
import com.shuvojitkar.tourist.R;

import java.util.ArrayList;
import java.util.List;

public class NearByPlaceDetailsMapActivity extends FragmentActivity implements OnMapReadyCallback, OnRouteFound {

    private GoogleMap mMap;
    private double nearbyplaceLat;
    private double nearbyplaceLan;
    private double mainLat;
    private double mainLan;
    private String newrbyplacename;
    private String main_place_name;


    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_place_details_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        newrbyplacename = getIntent().getStringExtra("NName");
        main_place_name = getIntent().getStringExtra("MName");
        nearbyplaceLan = getIntent().getDoubleExtra("NLan", 0);
        nearbyplaceLat = getIntent().getDoubleExtra("NLat", 0);
        mainLat = getIntent().getDoubleExtra("MLat", 0);
        mainLan = getIntent().getDoubleExtra("MLan", 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng nearbylatlan = new LatLng(nearbyplaceLat, nearbyplaceLan);
        mMap.addMarker(new MarkerOptions().position(nearbylatlan).title(newrbyplacename));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nearbylatlan));

        LatLng mainlatlan = new LatLng(mainLan, mainLat);
        mMap.addMarker(new MarkerOptions().position(mainlatlan).title(main_place_name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mainlatlan));
        try {
            String origin = mainLan + "," + mainLat;
            String desc = nearbyplaceLat + "," + nearbyplaceLan;
            new FindRoutsBetween(this, origin, desc).execute();
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onRouteFoundSuccess(List<Route> routes) {

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            /*originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                    .title(route.endAddress)
                    .position(route.endLocation)));*/

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            Distance distance = routes.get(routes.size() - 1).distance;
            Duration duration = routes.get(routes.size() - 1).duration;


            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));
            polylinePaths.add(mMap.addPolyline(polylineOptions));

            Toast.makeText(getBaseContext(), distance.text + distance.value, Toast.LENGTH_LONG).show();

            Toast.makeText(getBaseContext(), duration.text + duration.value, Toast.LENGTH_LONG).show();


        }
    }
}
