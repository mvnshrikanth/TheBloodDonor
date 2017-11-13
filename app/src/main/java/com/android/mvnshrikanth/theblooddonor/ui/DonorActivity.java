package com.android.mvnshrikanth.theblooddonor.ui;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.mvnshrikanth.theblooddonor.R;

public class DonorActivity extends AppCompatActivity {
    private static final String LOG_TAG = DonorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new DonorFragment()).commit();

    }
}
