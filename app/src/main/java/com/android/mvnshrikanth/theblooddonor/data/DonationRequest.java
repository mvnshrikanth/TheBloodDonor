package com.android.mvnshrikanth.theblooddonor.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mvnsh on 12/11/2017.
 */
@IgnoreExtraProperties
public class DonationRequest implements Parcelable {

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
    private String donationRequestKey;
    private String requesterUidKey;
    private String requesterName;
    private String requestedBloodType;
    private String requesterCity;
    private String requesterState;
    private String requesterZip;
    private String requestedDate;
    private String donorName;
    private String donorUidKey;
    private String donatedDate;
    private String donorResponseCount;

    public DonationRequest() {
    }

    protected DonationRequest(Parcel in) {
        donationRequestKey = in.readString();
        requesterUidKey = in.readString();
        requesterName = in.readString();
        requestedBloodType = in.readString();
        requesterCity = in.readString();
        requesterState = in.readString();
        requesterZip = in.readString();
        requestedDate = in.readString();
        donorName = in.readString();
        donorUidKey = in.readString();
        donatedDate = in.readString();
        donorResponseCount = in.readString();
    }

    public DonationRequest(String donationRequestKey, String requesterUidKey, String requesterName, String requestedBloodType, String requesterCity, String requesterState, String requesterZip, String requestedDate, String donorName, String donorUidKey, String donatedDate, String donorResponseCount) {
        this.donationRequestKey = donationRequestKey;
        this.requesterUidKey = requesterUidKey;
        this.requesterName = requesterName;
        this.requestedBloodType = requestedBloodType;
        this.requesterCity = requesterCity;
        this.requesterState = requesterState;
        this.requesterZip = requesterZip;
        this.requestedDate = requestedDate;
        this.donorName = donorName;
        this.donorUidKey = donorUidKey;
        this.donatedDate = donatedDate;
        this.donorResponseCount = donorResponseCount;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(donationRequestKey);
        dest.writeString(requesterUidKey);
        dest.writeString(requesterName);
        dest.writeString(requestedBloodType);
        dest.writeString(requesterCity);
        dest.writeString(requesterState);
        dest.writeString(requesterZip);
        dest.writeString(requestedDate);
        dest.writeString(donorName);
        dest.writeString(donorUidKey);
        dest.writeString(donatedDate);
        dest.writeString(donorResponseCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> results = new HashMap<>();
        results.put("donationRequestKey", donationRequestKey);
        results.put("requesterUidKey", requesterUidKey);
        results.put("requesterName", requesterName);
        results.put("requestedBloodType", requestedBloodType);
        results.put("requesterCity", requesterCity);
        results.put("requesterState", requesterState);
        results.put("requesterZip", requesterZip);
        results.put("requestedDate", requestedDate);
        results.put("donorName", donorName);
        results.put("donorUidKey", donorUidKey);
        results.put("donatedDate", donatedDate);
        results.put("donorResponseCount", donorResponseCount);
        return results;
    }

    public String getDonationRequestKey() {
        return donationRequestKey;
    }

    public void setDonationRequestKey(String donationRequestKey) {
        this.donationRequestKey = donationRequestKey;
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

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorUidKey() {
        return donorUidKey;
    }

    public void setDonorUidKey(String donorUidKey) {
        this.donorUidKey = donorUidKey;
    }

    public String getDonatedDate() {
        return donatedDate;
    }

    public void setDonatedDate(String donatedDate) {
        this.donatedDate = donatedDate;
    }

    public String getDonorResponseCount() {
        return donorResponseCount;
    }

    public void setDonorResponseCount(String donorResponseCount) {
        this.donorResponseCount = donorResponseCount;
    }
}
