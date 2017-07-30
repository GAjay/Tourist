package com.shuvojitkar.tourist.Map_Helpers;

/**
 * Created by Nuhel on 7/27/2017.
 */

import java.util.HashMap;


import android.util.Log;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

/**
 * Created by navneet on 23/7/16.
 */
public class NearByPlaceDataParser {

    public  ArrayList<PlaceDetails> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {

        }
        return getPlaces(jsonArray);
    }

    private  ArrayList<PlaceDetails> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();

        ArrayList<PlaceDetails> placeDetailslist = new ArrayList<>();
        List<HashMap<String, String>> placesList = new ArrayList<>();

        HashMap<String, String> placeMap = null;
        PlaceDetails placeDetails = new PlaceDetails();
        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {

                placeDetails = getPlace((JSONObject)jsonArray.get(i));
                placeDetailslist.add(placeDetails);

                //placeMap = getPlace((JSONObject)jsonArray.get(i));
               // placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placeDetailslist;
    }

    private PlaceDetails getPlace(JSONObject googlePlaceJson) {
        PlaceDetails placeDetails = new PlaceDetails();
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String latitude = "";
        String longitude = "";
        String reference = "";

        Log.d("getPlace", "Entered");

        try {

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
           // googlePlaceMap.put("place_name", placeName);
           // googlePlaceMap.put("vicinity", vicinity);

            placeDetails.setLat(latitude);
            placeDetails.setLan(longitude);
            placeDetails.setReference(reference);

          /*  googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);*/
            Log.d("getPlace", googlePlaceJson.getString("name"));
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return placeDetails;
    }
}
