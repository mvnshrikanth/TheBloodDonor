package com.android.mvnshrikanth.theblooddonor.utilities;

import com.android.mvnshrikanth.theblooddonor.data.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static final String USER_PROFILE_PICTURES_STORAGE_PATH = "user_profile_photos";
    private static final String ZIP_CODE_API_KEY = "hfkV4NMoQjXfJn4vXKPDhgCu9g1OTXw32zCKj4iYCrK919tgqT5kZuOcr4Cgt1y9";
    public static final String ZIP_CODE_API_BASE_URL = "https://www.zipcodeapi.com/rest/" + ZIP_CODE_API_KEY;

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);
        return simpleDateFormat.format(new Date());
    }

    public static String getDateAndTimeForDisplay(String date) {
        String displayString = null;

        try {
            SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.US);

            Date endDate = simpleDateFormat.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);

            if (simpleDateFormat.format(endDate).equals(simpleDateFormat.format(new Date()))) {
                displayString = simpleTimeFormat.format(simpleDateTimeFormat.parse(date));
            } else if (simpleDateFormat.format(endDate).equals(simpleDateFormat.format(calendar.getTime()))) {
                displayString = "Yesterday";
            } else {
                displayString = simpleDateFormat.format(endDate);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return displayString;
    }

    public static String getTimeForDisplay(String date) {
        String displayString = null;
        try {
            SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.US);
            Date endDate = simpleDateTimeFormat.parse(date);

            displayString = simpleTimeFormat.format(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return displayString;
    }

    public static String getDateForDisplay(String date) {
        String displayString = null;
        try {
            SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
            Date endDate = simpleDateTimeFormat.parse(date);

            displayString = simpleDateFormat.format(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return displayString;
    }

    public static Location getCityStateFromJSONString(String locationInfoStr) throws JSONException {
        JSONObject jsonObjectLocationInfo = new JSONObject(locationInfoStr);
        return new Location(jsonObjectLocationInfo.getString("city"),
                jsonObjectLocationInfo.getString("state"));
    }


}
