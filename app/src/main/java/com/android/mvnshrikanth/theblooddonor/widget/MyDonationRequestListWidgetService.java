package com.android.mvnshrikanth.theblooddonor.widget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.android.mvnshrikanth.theblooddonor.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST_SHARED_PREF_KEY;


public class MyDonationRequestListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            ArrayList<DonationRequest> myDonationRequestList = new ArrayList<>();

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                initRequest();
            }

            @Override
            public void onDestroy() {
                myDonationRequestList.clear();
            }

            @Override
            public int getCount() {
                if (myDonationRequestList != null) {
                    return myDonationRequestList.size();
                } else {
                    return 0;
                }
            }

            @Override
            public RemoteViews getViewAt(int i) {
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_my_donation_request_widget);
                DonationRequest donationRequest = myDonationRequestList.get(i);

                remoteViews.setTextViewText(R.id.textView_widget_blood_type, donationRequest.getRequestedBloodType());
                remoteViews.setTextViewText(R.id.textView_widget_requested_date, Utils.getDateForDisplay(donationRequest.getRequestedDate()));
                remoteViews.setTextViewText(R.id.textView_widget_response_count, donationRequest.getDonorResponseCount());

                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            private void initRequest() {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String json = sharedPreferences.getString(MY_DONATION_REQUEST_SHARED_PREF_KEY, "");
                if (!json.equals("")) {
                    Gson gson = new Gson();

                    myDonationRequestList = gson.fromJson(json, new TypeToken<ArrayList<DonationRequest>>() {
                    }.getType());

                }
            }
        };
    }
}
