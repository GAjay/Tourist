package com.shuvojitkar.tourist.Model;

/**
 * Created by SHOBOJIT on 8/3/2017.
 */

public class User_Post {
    String date;
    String description;
    String image;
    String title;
    String user_type;
    String userid;

    public User_Post(String date, String description, String image, String title, String user_type, String userid) {
        this.date = date;
        this.description = description;
        this.image = image;
        this.title = title;
        this.user_type = user_type;
        this.userid = userid;
    }

    public User_Post() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
