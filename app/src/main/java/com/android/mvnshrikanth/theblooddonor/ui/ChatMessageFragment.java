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
import com.android.mvnshrikanth.theblooddonor.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;

import static com.android.mvnshrikanth.theblooddonor.ui.ChatFragment.CHAT_ID_KEY;
import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST_KEY;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utils.Utils.CHAT_MESSAGES_PATH;

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

    public ChatMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_message, container, false);
        savedInstanceState = this.getArguments();

        String donationRequestKey = savedInstanceState.getString(MY_DONATION_REQUEST_KEY);
        final String mUid = savedInstanceState.getString(USER_ID);
        final String mUserName = savedInstanceState.getString(USERNAME);
        final String[] chatIdKey = {savedInstanceState.getString(CHAT_ID_KEY)};

        firebaseDatabase = FirebaseDatabase.getInstance();
        chatMessageDatabaseReference = firebaseDatabase.getReference().child(CHAT_MESSAGES_PATH);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatIdKey[0] == null) {
                    chatIdKey[0] = chatMessageDatabaseReference.push().getKey();
                }

                ChatMessage chatMessage = new ChatMessage(editTextChatMessage.getText().toString(), Utils.getCurrentDate(), mUid, mUserName, chatIdKey[0]);
                chatMessageDatabaseReference.child(chatIdKey[0]).setValue(chatMessage);
                editTextChatMessage.setText("");
            }
        });

        return view;
    }
}
