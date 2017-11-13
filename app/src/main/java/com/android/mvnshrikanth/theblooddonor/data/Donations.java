package com.android.mvnshrikanth.theblooddonor.data;

/**
 * Created by mvnsh on 11/5/2017.
 */

public class Donations {

    private String donorName;
    private String doneeName;
    private String donationDate;
    private String bloodType;

    public Donations(String donorName, String doneeName, String donationDate, String bloodType) {
        this.donorName = donorName;
        this.doneeName = doneeName;
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

}
