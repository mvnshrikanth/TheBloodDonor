package com.android.mvnshrikanth.theblooddonor.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.adapters.MyDonationsAdapter;
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

import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.MY_DONATIONS_PATH;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyDonationsFragment extends Fragment {

    @BindView(R.id.recyclerView_My_Donations)
    RecyclerView recyclerViewMyDonations;
    @BindView(R.id.empty_donation_view)
    View emptyView;
    private List<DonationRequest> myDonationList;
    private Unbinder unbinder;
    private DatabaseReference myDonationsDatabaseReference;
    private ChildEventListener myDonationsChildEventListener;
    private MyDonationsAdapter myDonationsAdapter;

    public MyDonationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_donations, container, false);
        unbinder = ButterKnife.bind(this, view);
        savedInstanceState = this.getArguments();
        String mUid = savedInstanceState.getString(USER_ID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


        myDonationList = new ArrayList<>();
        assert mUid != null;
        myDonationsDatabaseReference = firebaseDatabase.getReference().child(MY_DONATIONS_PATH).child(mUid);
        attachDatabaseReadListener();
        myDonationsAdapter = new MyDonationsAdapter(view.getContext());
        recyclerViewMyDonations.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL, false));
        recyclerViewMyDonations.setAdapter(myDonationsAdapter);
        toggleRecyclerView();
        return view;
    }

    private void attachDatabaseReadListener() {
        if (myDonationsChildEventListener == null) {
            myDonationsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DonationRequest myDonations = dataSnapshot.getValue(DonationRequest.class);
                    myDonationList.add(myDonations);
                    myDonationsAdapter.prepareMyDonationList(myDonationList);
                    toggleRecyclerView();
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
            myDonationsDatabaseReference.addChildEventListener(myDonationsChildEventListener);
        }
    }

    private void toggleRecyclerView() {
        if (myDonationList.size() > 0) {
            recyclerViewMyDonations.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            recyclerViewMyDonations.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void detachDatabaseReadListener() {
        if (myDonationsChildEventListener != null) {
            myDonationsDatabaseReference.removeEventListener(myDonationsChildEventListener);
            myDonationsChildEventListener = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        myDonationList.clear();
        detachDatabaseReadListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
