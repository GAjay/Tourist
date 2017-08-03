package com.shuvojitkar.tourist.Map_Helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shuvojitkar.tourist.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nuhel on 7/27/2017.
 */

public class GetNplaceDetails extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private String url;
    private PlaceDetails placeDetails;
    private Context context;
    private View view;

    @Override
    protected String doInBackground(Object... objects) {
        url = (String) objects[0];
        view = (View) objects[1];
        placeDetails = (PlaceDetails) objects[2];
        context = (Context) objects[3];

        DownloadNearbyPlaceUrl downloadURL = new DownloadNearbyPlaceUrl();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject js = new JSONObject(result);
            JSONObject jsr = js.getJSONObject("result");
            String s = jsr.getString("icon").toString();
            String phone = jsr.getString("formatted_phone_number");
            placeDetails.setPhoneNumber(phone);
            CircleImageView img = (CircleImageView) view.findViewById(R.id.list_place_image);
            Picasso.with(img.getContext()).load(s).into(img);
        } catch (JSONException e) {
            placeDetails.setPhoneNumber("-1");
        }
    }
}
