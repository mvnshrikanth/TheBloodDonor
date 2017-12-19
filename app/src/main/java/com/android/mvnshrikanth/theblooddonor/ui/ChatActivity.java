package com.android.mvnshrikanth.theblooddonor.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;

import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST_DATA;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        DonationRequest donationRequest = this.getIntent().getParcelableExtra(MY_DONATION_REQUEST_DATA);

        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MY_DONATION_REQUEST_DATA, donationRequest);
        chatFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_chat_container, chatFragment)
                .commit();

    }
}
