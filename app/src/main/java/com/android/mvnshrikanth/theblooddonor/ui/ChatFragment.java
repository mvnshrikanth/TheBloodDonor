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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST_DATA;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utils.Utils.DONATION_REQUESTS_CHATS;

public class ChatFragment extends Fragment implements ChatListAdapter.ChatListAdapterEventListener {

    @BindView(R.id.recyclerView_user_chat_list)
    RecyclerView recyclerViewUserChatList;
    private View view;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference donationRequestsChatsDatabaseReference;
    private ChildEventListener donationRequestsChatsChildEventListener;
    private List<ChatMessage> chatMessageList;
    private ChatListAdapter chatListAdapter;

    private String mUid;
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
        DonationRequest donationRequest = savedInstanceState.getParcelable(MY_DONATION_REQUEST_DATA);
        mUid = savedInstanceState.getString(USER_ID);

        chatMessageList = new ArrayList<ChatMessage>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        assert donationRequest != null;
        donationRequestsChatsDatabaseReference = firebaseDatabase.getReference().child(DONATION_REQUESTS_CHATS).child(donationRequest.getDonationRequestKey());

        attachDatabaseReadListener();
        chatListAdapter = new ChatListAdapter(ChatFragment.this);
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
                    chatMessageList.add(chatMessage);
                    chatListAdapter.prepareChatListAdapter(chatMessageList);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        chatMessageList.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (donationRequestsChatsChildEventListener != null) {
            donationRequestsChatsDatabaseReference.removeEventListener(donationRequestsChatsChildEventListener);
            donationRequestsChatsChildEventListener = null;
        }
    }

    @Override
    public void onClick(ChatMessage chatMessage) {
        Intent intent = new Intent(view.getContext(), ChatMessageActivity.class);
        startActivity(intent);
    }
}
