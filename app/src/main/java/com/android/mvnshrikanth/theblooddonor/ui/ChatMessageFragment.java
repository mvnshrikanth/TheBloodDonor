package com.android.mvnshrikanth.theblooddonor.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.ChatMessage;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.android.mvnshrikanth.theblooddonor.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;

import static com.android.mvnshrikanth.theblooddonor.ui.NewDonationsFragment.DONATION_REQUEST_DATA;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utils.Utils.CHAT_MESSAGES;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatMessageFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatMessageDatabaseReference;

    @BindView(R.id.recyclerView_chat_messages)
    RecyclerView recyclerViewChatMessage;
    @BindView(R.id.sendButton)
    Button buttonSend;
    @BindView(R.id.messageEditText)
    EditText editTextChatMessage;

    private View view;
    private String mUid;
    private String mUserName;

    public ChatMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_message, container, false);
        savedInstanceState = this.getArguments();
        final DonationRequest donationRequest = savedInstanceState.getParcelable(DONATION_REQUEST_DATA);

        firebaseDatabase = FirebaseDatabase.getInstance();
        chatMessageDatabaseReference = firebaseDatabase.getReference().child(CHAT_MESSAGES);

        mUid = savedInstanceState.getString(USER_ID);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessage chatMessage = new ChatMessage(editTextChatMessage.getText().toString(), Utils.getCurrentDate(), mUid, mUserName);
                chatMessageDatabaseReference.push().setValue(chatMessage);
                editTextChatMessage.setText("");
            }
        });

        return view;
    }

}
