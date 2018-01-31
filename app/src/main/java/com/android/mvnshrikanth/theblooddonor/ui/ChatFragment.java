package com.android.mvnshrikanth.theblooddonor.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.adapters.ChatListAdapter;
import com.android.mvnshrikanth.theblooddonor.data.ChatMessage;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST_DATA;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.DONATION_REQUESTS_CHATS_PATH;

public class ChatFragment extends Fragment implements ChatListAdapter.ChatListAdapterEventListener {

    public static final String CHAT_ID_KEY = "chat_id_key";

    @BindView(R.id.recyclerView_user_chat_list)
    RecyclerView recyclerViewUserChatList;

    private View view;
    private DatabaseReference donationRequestsChatsDatabaseReference;
    private ChildEventListener donationRequestsChatsChildEventListener;
    private List<ChatMessage> chatList;
    private ChatListAdapter chatListAdapter;

    private Unbinder unbinder;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);

        savedInstanceState = this.getArguments();
        String donationRequestKey;
        DonationRequest donationRequest = savedInstanceState.getParcelable(MY_DONATION_REQUEST_DATA);

        assert donationRequest != null;
        donationRequestKey = donationRequest.getDonationRequestKey();

        String mUid = savedInstanceState.getString(USER_ID);
        String mUserName = savedInstanceState.getString(USERNAME);

        chatList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        donationRequestsChatsDatabaseReference = firebaseDatabase.getReference().child(DONATION_REQUESTS_CHATS_PATH).child(donationRequestKey);

        attachDatabaseReadListener();
        chatListAdapter = new ChatListAdapter(ChatFragment.this, donationRequest, mUid, mUserName, view.getContext());
        recyclerViewUserChatList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewUserChatList.setAdapter(chatListAdapter);
        return view;
    }


    private void attachDatabaseReadListener() {

        if (donationRequestsChatsChildEventListener == null) {

            donationRequestsChatsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    chatList.add(chatMessage);
                    chatListAdapter.prepareChatListAdapter(chatList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);

                    for (ListIterator<ChatMessage> listIterator = chatList.listIterator(); listIterator.hasNext(); ) {
                        ChatMessage chatMessage1 = listIterator.next();
                        assert chatMessage != null;
                        if (chatMessage1.getChatUserId().equals(chatMessage.getChatUserId()) && chatMessage1.getChatUserName().equals(chatMessage.getChatUserName())) {
                            listIterator.remove();
                            listIterator.add(chatMessage);
                        }
                    }
                    chatListAdapter.prepareChatListAdapter(chatList);
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
            donationRequestsChatsDatabaseReference.addChildEventListener(donationRequestsChatsChildEventListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        chatList.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (donationRequestsChatsChildEventListener != null) {
            donationRequestsChatsDatabaseReference.removeEventListener(donationRequestsChatsChildEventListener);
            donationRequestsChatsChildEventListener = null;
        }
    }


    @Override
    public void onClick(DonationRequest donationRequest, String mUid, String mUserName, String chatIdKey) {
        Intent intent = new Intent(view.getContext(), ChatMessageActivity.class);
        intent.putExtra(CHAT_ID_KEY, chatIdKey);
        intent.putExtra(USER_ID, mUid);
        intent.putExtra(USERNAME, mUserName);
        intent.putExtra(MY_DONATION_REQUEST_DATA, donationRequest);
        startActivity(intent);
    }
}
