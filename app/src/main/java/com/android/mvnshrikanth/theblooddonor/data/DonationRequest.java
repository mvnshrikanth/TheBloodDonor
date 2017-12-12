package com.android.mvnshrikanth.theblooddonor.data;

/**
 * Created by mvnsh on 12/11/2017.
 */

public class DonationRequest {

    private String requestersUidKey;
    private String requestersName;
    private String requestedBlootType;
    private String requestersCity;
    private String requestersdState;
    private String requestersInZip;
    private String requestedDate;
    private String donatedDate;


    public DonationRequest(String requestersUidKey, String requestersName, String requestedBlootType, String requestersCity, String requestersdState, String requestersInZip, String requestedDate, String donatedDate) {
        this.requestersUidKey = requestersUidKey;
        this.requestersName = requestersName;
        this.requestedBlootType = requestedBlootType;
        this.requestersCity = requestersCity;
        this.requestersdState = requestersdState;
        this.requestersInZip = requestersInZip;
        this.requestedDate = requestedDate;
        this.donatedDate = donatedDate;
    }

    public String getRequestersUidKey() {
        return requestersUidKey;
    }

    public void setRequestersUidKey(String requestersUidKey) {
        this.requestersUidKey = requestersUidKey;
    }

    public String getRequestersName() {
        return requestersName;
    }

    public void setRequestersName(String requestersName) {
        this.requestersName = requestersName;
    }

    public String getRequestedBlootType() {
        return requestedBlootType;
    }

    public void setRequestedBlootType(String requestedBlootType) {
        this.requestedBlootType = requestedBlootType;
    }

    public String getRequestersCity() {
        return requestersCity;
    }

    public void setRequestersCity(String requestersCity) {
        this.requestersCity = requestersCity;
    }

    public String getRequestersdState() {
        return requestersdState;
    }

    public void setRequestersdState(String requestersdState) {
        this.requestersdState = requestersdState;
    }

    public String getRequestersInZip() {
        return requestersInZip;
    }

    public void setRequestersInZip(String requestersInZip) {
        this.requestersInZip = requestersInZip;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getDonatedDate() {
        return donatedDate;
    }

    public void setDonatedDate(String donatedDate) {
        this.donatedDate = donatedDate;
    }
}
