package com.android.mvnshrikanth.theblooddonor.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment;
import com.android.mvnshrikanth.theblooddonor.ui.MyDonationsFragment;
import com.android.mvnshrikanth.theblooddonor.ui.NewDonationsFragment;

/**
 * Created by mvnsh on 11/12/2017.
 */

public class OptionsAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = OptionsAdapter.class.getSimpleName();
    private Context context;


    public OptionsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            //TODO 2) Create my donations fragment
            case 0:
                MyDonationsFragment myDonationsFragment = new MyDonationsFragment();
                return myDonationsFragment;
            case 1:
                MyDonationRequestsFragment myDonationRequestsFragment = new MyDonationRequestsFragment();
                return myDonationRequestsFragment;
            //TODO 3) Create new donation requests fragment
            default:
                NewDonationsFragment newDonationsFragment = new NewDonationsFragment();
                return newDonationsFragment;
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
