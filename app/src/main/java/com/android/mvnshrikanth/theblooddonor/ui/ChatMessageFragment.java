package com.android.mvnshrikanth.theblooddonor.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.ImageButton;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.adapters.ChatMessageAdapter;
import com.android.mvnshrikanth.theblooddonor.data.ChatMessage;
import com.android.mvnshrikanth.theblooddonor.data.ChatUser;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.android.mvnshrikanth.theblooddonor.utilities.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.mvnshrikanth.theblooddonor.ui.ChatFragment.CHAT_ID_KEY;
import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.CHAT_MESSAGES_PATH;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.DONATION_CHAT_USER_PATH;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.DONATION_REQUESTS_CHATS_PATH;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.DONATION_REQUESTS_PATH;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.MY_DONATIONS_PATH;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.MY_DONATION_REQUESTS_PATH;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatMessageFragment extends Fragment {

    @BindView(R.id.recyclerView_chat_messages)
    RecyclerView recyclerViewChatMessage;
    @BindView(R.id.sendButton)
    Button buttonSend;
    @BindView(R.id.messageEditText)
    EditText editTextChatMessage;
    @BindView(R.id.button_approve_donor)
    ImageButton imageButtonApproveDonor;

    private Unbinder unbinder;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference chatMessagesDatabaseReference;
    private DatabaseReference chatIDDatabaseReference;
    private DatabaseReference chatUserIDDatabaseReference;
    private ValueEventListener chatIDValueEventListener;
    private ChildEventListener chatMessagesChildEventListener;
    private ValueEventListener chatUserIDvalueEventListener;

    private View mView;
    private String chatIdKey;
    private String donationRequestKey;
    private String mUid;
    private String mUserName;
    private String mDonorId;
    private String mDonorName;
    private ChatMessageAdapter chatMessageAdapter;
    private List<ChatMessage> chatMessageList;
    private Boolean blnNewChatId;
    private DonationRequest donationRequest;

    public ChatMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chat_message, container, false);
        unbinder = ButterKnife.bind(this, mView);
        savedInstanceState = this.getArguments();

        donationRequest = savedInstanceState.getParcelable(MY_DONATION_REQUEST);
        assert donationRequest != null;
        donationRequestKey = donationRequest.getDonationRequestKey();
        mUid = savedInstanceState.getString(USER_ID);
        mUserName = savedInstanceState.getString(USERNAME);
        chatIdKey = savedInstanceState.getString(CHAT_ID_KEY);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        chatIDDatabaseReference = firebaseDatabase.getReference().child(DONATION_CHAT_USER_PATH).child(donationRequestKey);
        chatUserIDDatabaseReference = firebaseDatabase.getReference().child(DONATION_CHAT_USER_PATH).child(donationRequestKey);

        if ((donationRequest.getDonatedDate() == null)) {
            if (donationRequest.getRequesterUidKey().equals(mUid)) {
                imageButtonApproveDonor.setVisibility(View.VISIBLE);
            }
        }

        blnNewChatId = false;
        attachDatabaseReadListener();
        chatMessageList = new ArrayList<ChatMessage>();

        editTextChatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    buttonSend.setEnabled(true);
                } else {
                    buttonSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageIdKey = databaseReference.child(CHAT_MESSAGES_PATH).child(chatIdKey).push().getKey();
                String donorId;
                String donorName;

                if (!mUid.equals(donationRequest.getRequesterUidKey())) {
                    donorId = mUid;
                    donorName = mUserName;
                } else {
                    donorId = mDonorId;
                    donorName = mDonorName;
                }

                ChatMessage chatMessage = new ChatMessage(
                        editTextChatMessage.getText().toString(),
                        Utils.getCurrentDate(),
                        mUid,
                        mUserName,
                        chatIdKey,
                        donorId,
                        donorName);

                Map<String, Object> chatValues = chatMessage.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/" + CHAT_MESSAGES_PATH + "/" + chatIdKey + "/" + messageIdKey, chatValues);
                childUpdates.put("/" + DONATION_REQUESTS_CHATS_PATH + "/" + donationRequestKey + "/" + chatIdKey, chatValues);
                databaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Snackbar.make(mView, "Issue while sending the message.", Snackbar.LENGTH_SHORT).show();
                        } else {
                            if (blnNewChatId) {
                                blnNewChatId = false;
                                assert donationRequestKey != null;
                                assert mUid != null;
                                ChatUser chatUser = new ChatUser(chatIdKey, mUid, mUserName);
                                Map<String, Object> chatUserValue = chatUser.toMap();
                                ChatMessageFragment.this.databaseReference.child(DONATION_CHAT_USER_PATH).child(donationRequestKey).child(mUid).setValue(chatUserValue);
                            }
                        }
                    }
                });

                editTextChatMessage.setText("");
                buttonSend.setEnabled(false);
            }
        });

        imageButtonApproveDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Complete Donation Request")
                        .setMessage(mDonorName + " will be the donor for your requested blood type.")
                        .setNegativeButton(R.string.str_dialog_cancel_request, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO 1) add cancellation message.
                            }
                        })
                        .setPositiveButton(R.string.str_dialog_complete_request, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                donationRequest.setDonatedDate(Utils.getCurrentDate());
                                donationRequest.setDonorName(mDonorName);
                                donationRequest.setDonorUidKey(mDonorId);
                                Map<String, Object> donorDetails = donationRequest.toMap();
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/" + MY_DONATIONS_PATH + "/" + mDonorId + "/" + donationRequestKey, donorDetails);
                                childUpdates.put("/" + MY_DONATION_REQUESTS_PATH + "/" + mUid + "/" + donationRequestKey, donorDetails);
                                childUpdates.put("/" + DONATION_REQUESTS_PATH + "/" + donationRequestKey, donorDetails);
                                databaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Snackbar.make(mView, "Issue while completing the request", Snackbar.LENGTH_SHORT).show();
                                        } else {
                                            Snackbar.make(mView, "Completed request successfully", Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                builder.create().show();
            }
        });

        chatMessageAdapter = new ChatMessageAdapter(mUid, mView.getContext());
        recyclerViewChatMessage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewChatMessage.setAdapter(chatMessageAdapter);

        return mView;
    }

    private void attachDatabaseReadListener() {
        if (chatIDValueEventListener == null) {
            chatIDValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(mUid)) {
                        ChatUser chatUser = dataSnapshot.child(mUid).getValue(ChatUser.class);
                        assert chatUser != null;
                        chatIdKey = chatUser.getChatIdKey();
                    }
                    if (chatIdKey == null) {
                        chatIdKey = databaseReference.child(CHAT_MESSAGES_PATH).push().getKey();
                        blnNewChatId = true;
                    }
                    chatMessagesDatabaseReference = firebaseDatabase.getReference().child(CHAT_MESSAGES_PATH).child(chatIdKey);
                    attachChatMessagesDatabaseReadListener();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            chatIDDatabaseReference.addListenerForSingleValueEvent(chatIDValueEventListener);
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
                    recyclerViewChatMessage.smoothScrollToPosition(chatMessageList.size());
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

        if (mUid.equals(donationRequest.getRequesterUidKey())) {

            Query query = chatUserIDDatabaseReference.orderByChild("chatIdKey").equalTo(chatIdKey);

            chatUserIDvalueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                        ChatUser chatUser = dataSnapshotChild.getValue(ChatUser.class);
                        assert chatUser != null;
                        mDonorId = chatUser.getDonorId();
                        mDonorName = chatUser.getDonorName();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            query.addListenerForSingleValueEvent(chatUserIDvalueEventListener);
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
        if (chatIDValueEventListener != null) {
            chatIDDatabaseReference.removeEventListener(chatIDValueEventListener);
            chatIDValueEventListener = null;
        }
        if (chatMessagesChildEventListener != null) {
            chatMessagesDatabaseReference.removeEventListener(chatMessagesChildEventListener);
            chatMessagesChildEventListener = null;
        }
        if (chatUserIDvalueEventListener != null) {
            chatUserIDDatabaseReference.removeEventListener(chatUserIDvalueEventListener);
            chatUserIDvalueEventListener = null;
        }
    }

}
