package com.shuvojitkar.tourist.Model;

/**
 * Created by SHOBOJIT on 7/24/2017.
 */

public class Home_frag_Model {
    private String image;
    private String name;
    private Double lang;
    private Double lat;

    public Home_frag_Model(String image, String name,Double lang,Double lat) {
        this.image = image;
        this.name = name;
        this.lang=lang;
        this.lat= lat;
    }

    public Home_frag_Model() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
