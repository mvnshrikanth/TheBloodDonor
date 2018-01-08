package com.android.mvnshrikanth.theblooddonor.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;

import static com.android.mvnshrikanth.theblooddonor.ui.ChatFragment.CHAT_ID_KEY;
import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;

public class ChatMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        DonationRequest donationRequest = this.getIntent().getParcelableExtra(MY_DONATION_REQUEST);
        String mUid = this.getIntent().getStringExtra(USER_ID);
        String mUserName = this.getIntent().getStringExtra(USERNAME);
        String chatIdKey = this.getIntent().getStringExtra(CHAT_ID_KEY);

        ChatMessageFragment chatMessageFragment = new ChatMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MY_DONATION_REQUEST, donationRequest);
        bundle.putString(USER_ID, mUid);
        bundle.putString(USERNAME, mUserName);
        bundle.putString(CHAT_ID_KEY, chatIdKey);
        chatMessageFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_chat_message_container, chatMessageFragment)
                .commit();
    }
}
