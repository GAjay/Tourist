package com.shuvojitkar.tourist.Model;

/**
 * Created by SHOBOJIT on 7/24/2017.
 */

public class Home_frag_Model {
    private String image;
    private String name;

    public Home_frag_Model(String image, String name) {
        this.image = image;
        this.name = name;
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
}
