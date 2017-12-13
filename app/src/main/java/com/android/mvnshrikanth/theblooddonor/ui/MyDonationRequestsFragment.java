package com.android.mvnshrikanth.theblooddonor.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.adapters.MyDonationRequestsAdapter;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.android.mvnshrikanth.theblooddonor.data.Users;
import com.android.mvnshrikanth.theblooddonor.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.mvnshrikanth.theblooddonor.data.DonationRequest.DONATION_REQUESTS_PATH;
import static com.android.mvnshrikanth.theblooddonor.data.Users.USERS_PATH;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;


public class MyDonationRequestsFragment extends Fragment {
    private static final String LOG_TAG = MyDonationRequestsFragment.class.getSimpleName();

    @BindView(R.id.recyclerView_My_Donation_Requests)
    RecyclerView recyclerView;
    @BindView(R.id.fab_request_donation)
    FloatingActionButton fabRequestDonation;
    List<DonationRequest> donationRequestList;
    MyDonationRequestsAdapter myDonationRequestsAdapter;
    private Unbinder unbinder;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference donationRequestsDBReference;
    private DatabaseReference userDBReference;
    private ValueEventListener userValueEventListener;

    private Users users;

    public MyDonationRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_requests, container, false);
        unbinder = ButterKnife.bind(this, view);

        savedInstanceState = this.getArguments();
        final String mUid = savedInstanceState.getString(USER_ID);

        firebaseDatabase = FirebaseDatabase.getInstance();
        donationRequestsDBReference = firebaseDatabase.getReference().child(DONATION_REQUESTS_PATH);
        userDBReference = firebaseDatabase.getReference().child(USERS_PATH).child(mUid);

        fabRequestDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachDatabaseReadListener();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] array = getResources().getStringArray(R.array.blood_type_array);
                final int[] selectedBloodType = new int[1];
                builder.setTitle("Select blood type for new donation request.");

                builder.setSingleChoiceItems(R.array.blood_type_array, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedBloodType[0] = which;
                    }
                });

                builder.setNegativeButton(R.string.str_dialog_cancel_request, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(), "Cancelled the request.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton(R.string.str_dialog_submit_new_request, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DonationRequest donationRequest =
                                new DonationRequest(mUid,
                                        users.getUserName(),
                                        array[selectedBloodType[0]],
                                        users.getCity(),
                                        users.getState(),
                                        users.getLocationZip(),
                                        Utils.getCurrentDate(),
                                        null);
                        donationRequestsDBReference.push().setValue(donationRequest);
                        Toast.makeText(getActivity().getApplicationContext(), "Submitted " + array[selectedBloodType[0]], Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }
        });



        myDonationRequestsAdapter = new MyDonationRequestsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(myDonationRequestsAdapter);

        return view;
    }

    private void attachDatabaseReadListener() {

        if (userValueEventListener == null) {
            userValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users = dataSnapshot.getValue(Users.class);
//                    Toast.makeText(getContext(), "Username " + users.getUserName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            userDBReference.addListenerForSingleValueEvent(userValueEventListener);
        }

    }


    private void detachDatabaseReadListener() {

        if (userValueEventListener != null) {
            userDBReference.removeEventListener(userValueEventListener);
            userValueEventListener = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        detachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
