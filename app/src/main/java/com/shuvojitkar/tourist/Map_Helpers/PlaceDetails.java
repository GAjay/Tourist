package com.shuvojitkar.tourist.Map_Helpers;

/**
 * Created by Nuhel on 7/30/2017.
 */

public class PlaceDetails {
    private String lan;
    private String lat;
    private String longName;
    private String shortName;
    private String address;
    private String reference;


    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    private String vicinity;


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    private String placeName;


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public Double getLan() {
        return Double.parseDouble(lan);
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public Double getLat() {
        return Double.parseDouble(lat);
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
