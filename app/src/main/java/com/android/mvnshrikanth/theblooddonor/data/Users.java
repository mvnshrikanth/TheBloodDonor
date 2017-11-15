package com.android.mvnshrikanth.theblooddonor.data;

/**
 * Created by mvnsh on 11/14/2017.
 */

public class Users {
    private String name;
    private String bloodType;
    private String locationZip;
    private String gender;
    private String photoUrl;

    public Users(String name, String bloodType, String locationZip, String gender, String photoUrl) {
        this.name = name;
        this.bloodType = bloodType;
        this.locationZip = locationZip;
        this.gender = gender;
        this.photoUrl = photoUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
