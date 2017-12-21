package com.android.mvnshrikanth.theblooddonor.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mvnsh on 12/14/2017.
 */

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

    protected ChatMessage(Parcel in) {
        messageText = in.readString();
        messageDate = in.readString();
        chatUserId = in.readString();
        chatUserName = in.readString();
        chatId = in.readString();
    }

    public ChatMessage(String messageText, String messageDate, String chatUserId, String chatUserName, String chatId) {
        this.messageText = messageText;
        this.messageDate = messageDate;
        this.chatUserId = chatUserId;
        this.chatUserName = chatUserName;
        this.chatId = chatId;
    }

    public ChatMessage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageText);
        dest.writeString(messageDate);
        dest.writeString(chatUserId);
        dest.writeString(chatUserName);
        dest.writeString(chatId);
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
}
