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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST_KEY;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utils.Utils.DONATION_REQUESTS_CHATS_PATH;

public class ChatFragment extends Fragment implements ChatListAdapter.ChatListAdapterEventListener {

    public static final String CHAT_ID_KEY = "chat_id_key";
    public static final String CHAT_LIST = "chat_list";
    @BindView(R.id.recyclerView_user_chat_list)
    RecyclerView recyclerViewUserChatList;
    private View view;
    private FirebaseDatabase firebaseDatabase;
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
        String donationRequestKey = savedInstanceState.getString(MY_DONATION_REQUEST_KEY);
        String mUid = savedInstanceState.getString(USER_ID);
        String mUserName = savedInstanceState.getString(USERNAME);

        chatList = new ArrayList<ChatMessage>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        assert donationRequestKey != null;
        donationRequestsChatsDatabaseReference = firebaseDatabase.getReference().child(DONATION_REQUESTS_CHATS_PATH).child(donationRequestKey);

        attachDatabaseReadListener();
        chatListAdapter = new ChatListAdapter(ChatFragment.this, donationRequestKey, mUid, mUserName);
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
    public void onClick(String donationRequestKey, String mUid, String mUserName, String chatIdKey) {
        Intent intent = new Intent(view.getContext(), ChatMessageActivity.class);
        intent.putExtra(CHAT_ID_KEY, chatIdKey);
        intent.putExtra(USER_ID, mUid);
        intent.putExtra(USERNAME, mUserName);
        intent.putExtra(MY_DONATION_REQUEST_KEY, donationRequestKey);
        startActivity(intent);
    }
}