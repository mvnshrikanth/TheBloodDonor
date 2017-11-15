package com.android.mvnshrikanth.theblooddonor.data;

/**
 * Created by mvnsh on 11/14/2017.
 */

public class Users {
    private String userName;
    private String bloodType;
    private String locationZip;
    private String gender;
    private String photoUrl;

    public Users() {
    }

    public Users(String userName, String bloodType, String locationZip, String gender, String photoUrl) {
        this.userName = userName;
        this.bloodType = bloodType;
        this.locationZip = locationZip;
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


}
