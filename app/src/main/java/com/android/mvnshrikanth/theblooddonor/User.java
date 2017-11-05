package com.android.mvnshrikanth.theblooddonor;

/**
 * Created by mvnsh on 11/5/2017.
 */

public class User {

    private String userName;
    private String name;
    private String bloodType;
    private String address;
    private String city;
    private String state;
    private String country;
    private int zip;
    private String sex;

    public User(String userName, String name, String bloodType, String address, String city, String state, String country, int zip, String sex) {
        this.userName = userName;
        this.name = name;
        this.bloodType = bloodType;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zip = zip;
        this.sex = sex;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String value) {
        this.userName = value;
    }

    public String getBloodType() {
        return this.bloodType;
    }

    public void setBloodType(String value) {
        this.bloodType = value;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String value) {
        this.city = value;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String value) {
        this.state = value;
    }

    public int getZip() {
        return this.zip;
    }

    public void setZip(int value) {
        this.zip = value;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String value) {
        this.sex = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
