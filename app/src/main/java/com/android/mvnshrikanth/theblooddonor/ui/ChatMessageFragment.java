package com.android.mvnshrikanth.theblooddonor.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.adapters.ChatMessageAdapter;
import com.android.mvnshrikanth.theblooddonor.data.ChatMessage;
import com.android.mvnshrikanth.theblooddonor.utils.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.mvnshrikanth.theblooddonor.ui.ChatFragment.CHAT_ID_KEY;
import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST_KEY;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utils.Utils.CHAT_MESSAGES_PATH;
import static com.android.mvnshrikanth.theblooddonor.utils.Utils.DONATION_CHAT_USER_PATH;
import static com.android.mvnshrikanth.theblooddonor.utils.Utils.DONATION_REQUESTS_CHATS_PATH;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatMessageFragment extends Fragment {

    public static final String CHAT_MESSAGE_LIST = "chat_message_list";

    @BindView(R.id.recyclerView_chat_messages)
    RecyclerView recyclerViewChatMessage;
    @BindView(R.id.sendButton)
    Button buttonSend;
    @BindView(R.id.messageEditText)
    EditText editTextChatMessage;
    private Unbinder unbinder;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference chatDatabaseReference;
    private DatabaseReference chatMessagesDatabaseReference;
    private DatabaseReference chatUserDatabaseReference;
    private ValueEventListener chatUserValueEventListener;
    private ChildEventListener chatMessagesChildEventListener;

    private View view;
    private String chatIdKey;
    private String donationRequestKey;
    private String mUid;
    private String mUserName;
    private ChatMessageAdapter chatMessageAdapter;
    private List<ChatMessage> chatMessageList;
    private Boolean blnNewChatId;

    public ChatMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_message, container, false);
        unbinder = ButterKnife.bind(this, view);
        savedInstanceState = this.getArguments();

        donationRequestKey = savedInstanceState.getString(MY_DONATION_REQUEST_KEY);
        mUid = savedInstanceState.getString(USER_ID);
        mUserName = savedInstanceState.getString(USERNAME);
        chatIdKey = savedInstanceState.getString(CHAT_ID_KEY);

        firebaseDatabase = FirebaseDatabase.getInstance();
        chatDatabaseReference = firebaseDatabase.getReference();
        chatUserDatabaseReference = firebaseDatabase.getReference().child(DONATION_CHAT_USER_PATH).child(donationRequestKey);

        blnNewChatId = false;
        attachDatabaseReadListener();

        chatMessageList = new ArrayList<ChatMessage>();

        editTextChatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    buttonSend.setEnabled(true);
                }
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageIdKey = chatDatabaseReference.child(CHAT_MESSAGES_PATH).child(chatIdKey).push().getKey();
                ChatMessage chatMessage = new ChatMessage(editTextChatMessage.getText().toString(),
                        Utils.getCurrentDate(), mUid, mUserName, chatIdKey);
                Map<String, Object> chatValues = chatMessage.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/" + CHAT_MESSAGES_PATH + "/" + chatIdKey + "/" + messageIdKey, chatValues);
                childUpdates.put("/" + DONATION_REQUESTS_CHATS_PATH + "/" + donationRequestKey + "/" + chatIdKey, chatValues);
                chatDatabaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Snackbar.make(view, "Issue while sending the message.", Snackbar.LENGTH_SHORT).show();
                        } else {
                            if (blnNewChatId) {
                                blnNewChatId = false;
                                assert donationRequestKey != null;
                                assert mUid != null;
                                Map<String, String> chatUserValue = new HashMap<>();
                                chatUserValue.put(chatIdKey, "true");
                                chatDatabaseReference.child(DONATION_CHAT_USER_PATH).child(donationRequestKey).child(mUid).setValue(chatUserValue);
                            }
                        }
                    }
                });

                editTextChatMessage.setText("");
                buttonSend.setEnabled(false);
            }
        });

        chatMessageAdapter = new ChatMessageAdapter(mUid);
        recyclerViewChatMessage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewChatMessage.setAdapter(chatMessageAdapter);

        return view;
    }

    private void attachDatabaseReadListener() {
        if (chatUserValueEventListener == null) {
            chatUserValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(mUid)) {
                        for (DataSnapshot dataSnapshotChildren : dataSnapshot.child(mUid).getChildren()) {
                            chatIdKey = dataSnapshotChildren.getKey();
                        }
                    }
                    if (chatIdKey == null) {
                        chatIdKey = chatDatabaseReference.child(CHAT_MESSAGES_PATH).push().getKey();
                        blnNewChatId = true;
                    }
                    chatMessagesDatabaseReference = firebaseDatabase.getReference().child(CHAT_MESSAGES_PATH).child(chatIdKey);
                    attachChatMessagesDatabaseReadListener();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            chatUserDatabaseReference.addListenerForSingleValueEvent(chatUserValueEventListener);
        }
    }

    private void attachChatMessagesDatabaseReadListener() {

        if (chatMessagesChildEventListener == null) {
            chatMessagesChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    chatMessageList.add(chatMessage);
                    chatMessageAdapter.prepareChatMessageList(chatMessageList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            chatMessagesDatabaseReference.addChildEventListener(chatMessagesChildEventListener);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        chatMessageList.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (chatUserValueEventListener != null) {
            chatUserDatabaseReference.removeEventListener(chatUserValueEventListener);
            chatUserValueEventListener = null;
        }
        if (chatMessagesChildEventListener != null) {
            chatMessagesDatabaseReference.removeEventListener(chatMessagesChildEventListener);
            chatMessagesChildEventListener = null;
        }
    }

}
