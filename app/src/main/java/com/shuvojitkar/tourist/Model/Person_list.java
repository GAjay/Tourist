package com.shuvojitkar.tourist.Model;

/**
 * Created by SHOBOJIT on 7/29/2017.
 */

public class Person_list {
    String deviceToken;
    String email;
    String image;
    String name;
    String password;
    String status;
    String type;

    public Person_list(String deviceToken, String email, String image, String name, String password, String status, String type) {
        this.deviceToken = deviceToken;
        this.email = email;
        this.image = image;
        this.name = name;
        this.password = password;
        this.status = status;
        this.type = type;
    }

    public Person_list() {
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
