package com.android.mvnshrikanth.theblooddonor.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage implements Parcelable {

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };
    private String messageText;
    private String messageDate;
    private String chatUserId;
    private String chatUserName;
    private String chatId;
    private String donorId;
    private String donorName;

    public ChatMessage() {
    }

    protected ChatMessage(Parcel in) {
        messageText = in.readString();
        messageDate = in.readString();
        chatUserId = in.readString();
        chatUserName = in.readString();
        chatId = in.readString();
        donorId = in.readString();
        donorName = in.readString();
    }

    public ChatMessage(String messageText, String messageDate, String chatUserId, String chatUserName, String chatId, String donorId, String donorName) {
        this.messageText = messageText;
        this.messageDate = messageDate;
        this.chatUserId = chatUserId;
        this.chatUserName = chatUserName;
        this.chatId = chatId;
        this.donorId = donorId;
        this.donorName = donorName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageText);
        dest.writeString(messageDate);
        dest.writeString(chatUserId);
        dest.writeString(chatUserName);
        dest.writeString(chatId);
        dest.writeString(donorId);
        dest.writeString(donorName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> results = new HashMap<>();
        results.put("messageText", messageText);
        results.put("messageDate", messageDate);
        results.put("chatUserId", chatUserId);
        results.put("chatUserName", chatUserName);
        results.put("chatId", chatId);
        results.put("donorId", donorId);
        results.put("donorName", donorName);
        return results;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(String chatUserId) {
        this.chatUserId = chatUserId;
    }

    public String getChatUserName() {
        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {
        this.chatUserName = chatUserName;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }
}
