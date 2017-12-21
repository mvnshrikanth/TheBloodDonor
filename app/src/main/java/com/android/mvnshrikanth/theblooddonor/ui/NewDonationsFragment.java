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
import com.android.mvnshrikanth.theblooddonor.adapters.DonationRequestAdapter;
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

import static com.android.mvnshrikanth.theblooddonor.ui.MyDonationRequestsFragment.MY_DONATION_REQUEST_KEY;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utils.Utils.DONATION_REQUESTS_PATH;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewDonationsFragment extends Fragment implements DonationRequestAdapter.DonationRequestAdapterOnClickListener {

    @BindView(R.id.recyclerView_new_donations)
    RecyclerView recyclerViewNewDonations;
    Unbinder unbinder;
    @BindView(R.id.empty_new_donation_view)
    View emptyView;
    View view;

    private String mUid;
    private String mUserName;
    private List<DonationRequest> donationRequestList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference donationRequestDBReference;
    private ChildEventListener donationRequestChildEventListener;
    private DonationRequestAdapter donationRequestAdapter;


    public NewDonationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_donations, container, false);
        unbinder = ButterKnife.bind(this, view);

        savedInstanceState = this.getArguments();
        mUid = savedInstanceState.getString(USER_ID);
        mUserName = savedInstanceState.getString(USERNAME);

        firebaseDatabase = FirebaseDatabase.getInstance();
        donationRequestDBReference = firebaseDatabase.getReference().child(DONATION_REQUESTS_PATH);
        donationRequestAdapter = new DonationRequestAdapter(NewDonationsFragment.this, mUid, mUserName);

        donationRequestList = new ArrayList<DonationRequest>();

        attachDatabaseReadListener();

        recyclerViewNewDonations.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewNewDonations.setAdapter(donationRequestAdapter);

        toggleRecyclerView();
        return view;
    }

    private void toggleRecyclerView() {
        if (donationRequestList.size() > 0) {
            recyclerViewNewDonations.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            recyclerViewNewDonations.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void attachDatabaseReadListener() {

        if (donationRequestChildEventListener == null) {
            donationRequestChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DonationRequest donationRequest = dataSnapshot.getValue(DonationRequest.class);
                    donationRequestList.add(donationRequest);
                    donationRequestAdapter.prepareDonationRequest(donationRequestList);
                    toggleRecyclerView();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //TODO remove is not tested.
                    DonationRequest donationRequest = dataSnapshot.getValue(DonationRequest.class);
                    donationRequestList.remove(donationRequest);
                    donationRequestAdapter.prepareDonationRequest(donationRequestList);
                    toggleRecyclerView();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            donationRequestDBReference.addChildEventListener(donationRequestChildEventListener);
        }

    }

    private void detachDatabaseReadListener() {
        if (donationRequestChildEventListener != null) {
            donationRequestDBReference.removeEventListener(donationRequestChildEventListener);
            donationRequestChildEventListener = null;
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
        donationRequestList.clear();
        detachDatabaseReadListener();
    }

    @Override
    public void onClick(String donationRequestKey, String mUid, String mUserName) {
        //TODO get the chat id on click and pass it to the chat message activity.
        Intent intent = new Intent(view.getContext(), ChatMessage.class);
        intent.putExtra(MY_DONATION_REQUEST_KEY, donationRequestKey);
        intent.putExtra(USER_ID, mUid);
        intent.putExtra(USERNAME, mUserName);
        startActivity(intent);
    }
}
