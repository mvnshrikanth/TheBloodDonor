package com.android.mvnshrikanth.theblooddonor.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.ui.DonorFragment;

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

    //TODO create separate fragments for each type of category and replace the fragment calls.
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                DonorFragment donorFragment = new DonorFragment();
                return donorFragment;
            case 2:
                DonorFragment donorFragment1 = new DonorFragment();
                return donorFragment1;
            default:
                DonorFragment donorFragment2 = new DonorFragment();
                return donorFragment2;
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
