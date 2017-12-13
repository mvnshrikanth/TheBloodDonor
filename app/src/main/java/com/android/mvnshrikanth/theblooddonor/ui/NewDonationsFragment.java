package com.android.mvnshrikanth.theblooddonor.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.adapters.DonationRequestAdapter;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.mvnshrikanth.theblooddonor.data.DonationRequest.DONATION_REQUESTS_PATH;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewDonationsFragment extends Fragment {

    private static final String DONATION_REQUEST_LIST_KEY = "donation_request_list_key";

    @BindView(R.id.recyclerView_new_donations)
    RecyclerView recyclerViewNewDonations;
    Unbinder unbinder;

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
        View view = inflater.inflate(R.layout.fragment_new_donations, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            donationRequestList = (List<DonationRequest>) savedInstanceState.getSerializable(DONATION_REQUEST_LIST_KEY);
        } else {
            donationRequestList = new ArrayList<DonationRequest>();
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        donationRequestDBReference = firebaseDatabase.getReference().child(DONATION_REQUESTS_PATH);
        donationRequestAdapter = new DonationRequestAdapter();
        recyclerViewNewDonations.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewNewDonations.setAdapter(donationRequestAdapter);

        donationRequestAdapter.prepareDonationRequest(donationRequestList);
        attachDatabaseReadListener();
        return view;
    }

    private void attachDatabaseReadListener() {

        if (donationRequestChildEventListener == null) {
            donationRequestChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DonationRequest donationRequest = dataSnapshot.getValue(DonationRequest.class);
                    donationRequestList.add(donationRequest);
                    donationRequestAdapter.prepareDonationRequest(donationRequestList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    DonationRequest donationRequest = dataSnapshot.getValue(DonationRequest.class);
                    donationRequestList.remove(donationRequest);
                    donationRequestAdapter.prepareDonationRequest(donationRequestList);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DONATION_REQUEST_LIST_KEY, (Serializable) donationRequestList);
    }
}
