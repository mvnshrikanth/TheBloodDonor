package com.android.mvnshrikanth.theblooddonor.data;

/**
 * Created by mvnsh on 11/14/2017.
 */

public class Users {
    private String userName;
    private String bloodType;
    private String locationZip;
    private String city;
    private String state;
    private String country;
    private String gender;
    private String photoUrl;

    public Users() {
    }

    public Users(String userName, String bloodType, String locationZip, String city, String state, String country, String gender, String photoUrl) {
        this.userName = userName;
        this.bloodType = bloodType;
        this.locationZip = locationZip;
        this.city = city;
        this.state = state;
        this.country = country;
        this.gender = gender;
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getLocationZip() {
        return locationZip;
    }

    public void setLocationZip(String locationZip) {
        this.locationZip = locationZip;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
