package com.android.mvnshrikanth.theblooddonor.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment;
import com.android.mvnshrikanth.theblooddonor.ui.MyDonationsFragment;
import com.android.mvnshrikanth.theblooddonor.ui.NewDonationsFragment;

import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;

/**
 * Created by mvnsh on 11/12/2017.
 */

public class OptionsAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = OptionsAdapter.class.getSimpleName();
    private Context context;
    private String mUid;
    private String mUsername;

    public OptionsAdapter(Context context, FragmentManager fm, String mUid, String mUsername) {
        super(fm);
        this.context = context;
        this.mUid = mUid;
        this.mUsername = mUsername;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyDonationsFragment myDonationsFragment = new MyDonationsFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString(USER_ID, mUid);
                myDonationsFragment.setArguments(bundle1);
                return myDonationsFragment;
            case 1:
                MyDonationRequestsFragment myDonationRequestsFragment = new MyDonationRequestsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(USER_ID, mUid);
                bundle.putString(USERNAME, mUsername);
                myDonationRequestsFragment.setArguments(bundle);
                return myDonationRequestsFragment;
            case 2:
                NewDonationsFragment newDonationsFragment = new NewDonationsFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString(USER_ID, mUid);
                bundle2.putString(USERNAME, mUsername);
                newDonationsFragment.setArguments(bundle2);
                return newDonationsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.str_tab_my_donations);
        } else if (position == 1) {
            return context.getString(R.string.str_tab_requested_donations);
        } else {
            return context.getString(R.string.str_tab_new_donations);
        }
    }
}
