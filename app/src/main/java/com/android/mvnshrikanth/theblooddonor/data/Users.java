package com.android.mvnshrikanth.theblooddonor.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by mvnsh on 11/14/2017.
 */

public class Users implements Parcelable {
    public static final String USERS_PATH = "users";
    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
    @Exclude
    private String uidKey;
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

    public Users(String uidKey, String userName, String bloodType, String locationZip, String city, String state, String country, String gender, String photoUrl) {
        this.uidKey = uidKey;
        this.userName = userName;
        this.bloodType = bloodType;
        this.locationZip = locationZip;
        this.city = city;
        this.state = state;
        this.country = country;
        this.gender = gender;
        this.photoUrl = photoUrl;
    }

    protected Users(Parcel in) {
        uidKey = in.readString();
        userName = in.readString();
        bloodType = in.readString();
        locationZip = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        gender = in.readString();
        photoUrl = in.readString();
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


    public String getUidKey() {
        return uidKey;
    }

    public void setUidKey(String uidKey) {
        this.uidKey = uidKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(uidKey);
        dest.writeString(userName);
        dest.writeString(bloodType);
        dest.writeString(locationZip);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(gender);
        dest.writeString(photoUrl);
    }
}
