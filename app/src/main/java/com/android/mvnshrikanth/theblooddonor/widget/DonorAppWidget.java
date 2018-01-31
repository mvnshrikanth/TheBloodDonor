package com.android.mvnshrikanth.theblooddonor.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class DonorAppWidget extends AppWidgetProvider {

    public static final String KEY_POSITION = "position";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.donor_app_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        Intent intentDonationRequestsListWidget = new Intent(context, MyDonationRequestListWidgetService.class);
        intentDonationRequestsListWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intentDonationRequestsListWidget.putExtra("Random", Math.random() * 1000); // Add a random integer to stop the Intent being ignored.  This is needed for some API levels due to intent caching
        intentDonationRequestsListWidget.setData(Uri.parse(intentDonationRequestsListWidget.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(R.id.listView_widget_my_donation_requests, intentDonationRequestsListWidget);
        views.setEmptyView(R.id.listView_widget_my_donation_requests, R.id.empty_view);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView_widget_my_donation_requests);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

