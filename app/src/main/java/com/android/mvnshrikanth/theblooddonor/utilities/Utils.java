package com.android.mvnshrikanth.theblooddonor.utilities;

import android.util.Log;

import com.android.mvnshrikanth.theblooddonor.BuildConfig;
import com.android.mvnshrikanth.theblooddonor.data.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class Utils {

    public static final String USERS_PATH = "users";
    public static final String DONATION_REQUESTS_PATH = "donationRequests";
    public static final String MY_DONATION_REQUESTS_PATH = "myDonationRequests";
    public static final String MY_DONATIONS_PATH = "myDonations";
    public static final String CHAT_MESSAGES_PATH = "chatMessages";
    public static final String DONATION_REQUESTS_CHATS_PATH = "donationRequestChats";
    public static final String DONATION_CHAT_USER_PATH = "donationChatUser";
    public static final String USER_PROFILE_PICTURES_STORAGE_PATH = "user_profile_photos";
    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static final String ZIP_CODE_API_KEY = BuildConfig.API_KEY;

    public static final String ZIP_CODE_API_BASE_URL = "https://www.zipcodeapi.com/rest/" + ZIP_CODE_API_KEY;

    public Utils() {
        throw new AssertionError();
    }

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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yy", Locale.US);
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

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();

        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0) {
                return null;
            }

            return stringBuffer.toString();

        } finally {

            httpURLConnection.disconnect();

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

}
