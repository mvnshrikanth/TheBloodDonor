package com.android.mvnshrikanth.theblooddonor.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;

import static com.android.mvnshrikanth.theblooddonor.ui.NewDonationsFragment.DONATION_REQUEST_DATA;


public class ChatMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        DonationRequest donationRequest = this.getIntent().getParcelableExtra(DONATION_REQUEST_DATA);

        ChatMessageFragment chatMessageFragment = new ChatMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DONATION_REQUEST_DATA, donationRequest);
        chatMessageFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_chat_message_container, chatMessageFragment)
                .commit();


    }
}
