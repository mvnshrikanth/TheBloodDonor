package com.android.mvnshrikanth.theblooddonor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mvnsh on 12/12/2017.
 */

public class Utils {
    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        Date date = new Date();
        String currDate = simpleDateFormat.format(new Date());
        return currDate;
    }

}
