package com.android.mvnshrikanth.theblooddonor;

/**
 * Created by mvnsh on 11/5/2017.
 */

public class Donations {

    private String donorName;
    private String donorUserName;
    private String doneeName;
    private String doneeUserName;
    private String donationDate;
    private String bloodType;

    public Donations(String donorName, String donorUserName, String doneeName, String doneeUserName, String donationDate, String bloodType) {
        this.donorName = donorName;
        this.donorUserName = donorUserName;
        this.doneeName = doneeName;
        this.doneeUserName = doneeUserName;
        this.donationDate = donationDate;
        this.bloodType = bloodType;
    }

    public String getDonorName() {
        return this.donorName;
    }

    public void setDonorName(String value) {
        this.donorName = value;
    }

    public String getDoneeName() {
        return this.doneeName;
    }

    public void setDoneeName(String value) {
        this.doneeName = value;
    }

    public String getDonationDate() {
        return this.donationDate;
    }

    public void setDonationDate(String value) {
        this.donationDate = value;
    }

    public String getBloodType() {
        return this.bloodType;
    }

    public void setBloodType(String value) {
        this.bloodType = value;
    }

    public String getDonorUserName() {
        return donorUserName;
    }

    public void setDonorUserName(String donorUserName) {
        this.donorUserName = donorUserName;
    }

    public String getDoneeUserName() {
        return doneeUserName;
    }

    public void setDoneeUserName(String doneeUserName) {
        this.doneeUserName = doneeUserName;
    }
}
