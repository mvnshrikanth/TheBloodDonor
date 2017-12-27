package com.android.mvnshrikanth.theblooddonor.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);
        return simpleDateFormat.format(new Date());
    }

    public static String getDateAndTimeForDisplay(String date) {
        //TODO 1) Change to show based on date.
        String displayString = null;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);

            Date endDate = simpleDateFormat.parse(simpleDateFormat.format(date));
//            Date startDate = simpleDateTimeFormat.parse(new Date());

//            long diffMillisec = startDate.getTime() - endDate.getTime();
//
//            long diffSeconds = diffMillisec / 1000 % 60;
//            long diffMinutes = diffMillisec / (60 * 1000) % 60;
//            long diffHours = diffMillisec / (60 * 60 * 1000) % 24;
//            long diffDays = diffMillisec / (24 * 60 * 60 * 1000);
            if (Objects.equals(simpleDateFormat.format(endDate), simpleDateFormat.format(new Date()))) {
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.US);
                displayString = simpleTimeFormat.format(date);
            } else {
                displayString = "Yesterday";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return displayString;
    }

}
