package com.android.mvnshrikanth.theblooddonor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mvnsh on 12/12/2017.
 */

public class Utils {
    public static final String USERS_PATH = "users";
    public static final String DONATION_REQUESTS_PATH = "donationRequests";
    public static final String MY_DONATION_REQUESTS_PATH = "myDonationRequests";
    public static final String MY_DONATIONS_PATH = "myDonations";
    public static final String CHAT_MESSAGES_PATH = "chatMessages";
    public static final String DONATION_REQUESTS_CHATS_PATH = "donationRequestChats";
    public static final String DONATION_CHAT_USER_PATH = "donationChatUser";


    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        Date date = new Date();
        return simpleDateFormat.format(new Date());
    }

}
