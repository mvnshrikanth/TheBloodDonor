package com.android.mvnshrikanth.theblooddonor.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mvnsh on 1/9/2018.
 */

public class ChatUser implements Parcelable {

    public static final Creator<ChatUser> CREATOR = new Creator<ChatUser>() {
        @Override
        public ChatUser createFromParcel(Parcel in) {
            return new ChatUser(in);
        }

        @Override
        public ChatUser[] newArray(int size) {
            return new ChatUser[size];
        }
    };
    private String chatIdKey;
    private String donorId;
    private String donorName;

    public ChatUser(String chatIdKey, String donorId, String donorName) {
        this.chatIdKey = chatIdKey;
        this.donorId = donorId;
        this.donorName = donorName;
    }

    protected ChatUser(Parcel in) {
        chatIdKey = in.readString();
        donorId = in.readString();
        donorName = in.readString();
    }

    public ChatUser() {
    }

    public String getChatIdKey() {
        return chatIdKey;
    }

    public void setChatIdKey(String chatIdKey) {
        this.chatIdKey = chatIdKey;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(chatIdKey);
        parcel.writeString(donorId);
        parcel.writeString(donorName);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> results = new HashMap<>();
        results.put("chatIdKey", chatIdKey);
        results.put("donorId", donorId);
        results.put("donorName", donorName);
        return results;
    }
}
