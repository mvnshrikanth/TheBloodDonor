package com.android.mvnshrikanth.theblooddonor.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mvnsh on 12/11/2017.
 */

public class DonationRequest implements Parcelable {

    public static final String DONATION_REQUESTS_PATH = "donationRequests";
    public static final Creator<DonationRequest> CREATOR = new Creator<DonationRequest>() {
        @Override
        public DonationRequest createFromParcel(Parcel in) {
            return new DonationRequest(in);
        }

        @Override
        public DonationRequest[] newArray(int size) {
            return new DonationRequest[size];
        }
    };
    private String requesterUidKey;
    private String requesterName;
    private String requestedBloodType;
    private String requesterCity;
    private String requesterState;
    private String requesterZip;
    private String requestedDate;
    private String donatedDate;

    public DonationRequest() {
    }

    public DonationRequest(String requesterUidKey, String requesterName, String requestedBloodType, String requesterCity, String requesterState, String requesterZip, String requestedDate, String donatedDate) {
        this.requesterUidKey = requesterUidKey;
        this.requesterName = requesterName;
        this.requestedBloodType = requestedBloodType;
        this.requesterCity = requesterCity;
        this.requesterState = requesterState;
        this.requesterZip = requesterZip;
        this.requestedDate = requestedDate;
        this.donatedDate = donatedDate;
    }

    protected DonationRequest(Parcel in) {
        requesterUidKey = in.readString();
        requesterName = in.readString();
        requestedBloodType = in.readString();
        requesterCity = in.readString();
        requesterState = in.readString();
        requesterZip = in.readString();
        requestedDate = in.readString();
        donatedDate = in.readString();
    }

    public String getRequesterUidKey() {
        return requesterUidKey;
    }

    public void setRequesterUidKey(String requesterUidKey) {
        this.requesterUidKey = requesterUidKey;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequestedBloodType() {
        return requestedBloodType;
    }

    public void setRequestedBloodType(String requestedBloodType) {
        this.requestedBloodType = requestedBloodType;
    }

    public String getRequesterCity() {
        return requesterCity;
    }

    public void setRequesterCity(String requesterCity) {
        this.requesterCity = requesterCity;
    }

    public String getRequesterState() {
        return requesterState;
    }

    public void setRequesterState(String requesterState) {
        this.requesterState = requesterState;
    }

    public String getRequesterZip() {
        return requesterZip;
    }

    public void setRequesterZip(String requesterZip) {
        this.requesterZip = requesterZip;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requesterUidKey);
        dest.writeString(requesterName);
        dest.writeString(requestedBloodType);
        dest.writeString(requesterCity);
        dest.writeString(requesterState);
        dest.writeString(requesterZip);
        dest.writeString(requestedDate);
        dest.writeString(donatedDate);
    }
}
