package com.android.mvnshrikanth.theblooddonor.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.adapters.MyDonationRequestsAdapter;
import com.android.mvnshrikanth.theblooddonor.data.DonationRequest;
import com.android.mvnshrikanth.theblooddonor.data.Users;
import com.android.mvnshrikanth.theblooddonor.utilities.Utils;
import com.android.mvnshrikanth.theblooddonor.widget.DonorAppWidget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.DONATION_REQUESTS_PATH;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.MY_DONATION_REQUESTS_PATH;
import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.USERS_PATH;


public class MyDonationRequestsFragment extends Fragment implements MyDonationRequestsAdapter.MyDonationRequestAdapterOnClickListener {

    public static final String MY_DONATION_REQUEST_DATA = "my_donation_request";
    public static final String MY_DONATION_REQUEST_SHARED_PREF_KEY = "my_donation_request_shared_pref_key";

    @BindView(R.id.recyclerView_My_Donation_Requests)
    RecyclerView recyclerViewMyDonationsRequests;
    @BindView(R.id.empty_my_donation_request_view)
    View emptyView;
    @BindView(R.id.fab_request_donation)
    FloatingActionButton fabRequestDonation;

    private MyDonationRequestsAdapter myDonationRequestsAdapter;
    private View view;
    private Unbinder unbinder;
    private DatabaseReference donationRequestsDBReference;
    private DatabaseReference myDonationRequestDBReference;
    private ChildEventListener myDonationRequestChildEventListener;
    private DatabaseReference userDBReference;
    private ValueEventListener userValueEventListener;
    private List<DonationRequest> myDonationRequestList;
    private Users users;
    private String mUid;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyDonationRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_donation_requests, container, false);
        unbinder = ButterKnife.bind(this, view);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());

        savedInstanceState = this.getArguments();
        mUid = savedInstanceState.getString(USER_ID);
        String mUserName = savedInstanceState.getString(USERNAME);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        donationRequestsDBReference = firebaseDatabase.getReference();
        assert mUid != null;
        userDBReference = firebaseDatabase.getReference().child(USERS_PATH).child(mUid);
        myDonationRequestDBReference = firebaseDatabase.getReference().child(MY_DONATION_REQUESTS_PATH).child(mUid);

        myDonationRequestList = new ArrayList<>();

        attachDatabaseReadListener();

        fabRequestDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] array = getResources().getStringArray(R.array.blood_type_array);
                final int[] selectedBloodType = new int[1];
                builder.setTitle(R.string.str_alertdialog_title_donation_request);

                builder.setSingleChoiceItems(R.array.blood_type_array, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedBloodType[0] = which;
                    }
                });

                builder.setNegativeButton(R.string.str_dialog_cancel_request, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity().getApplicationContext(), "Cancelled the request.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton(R.string.str_dialog_submit_new_request, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String key = donationRequestsDBReference.child(DONATION_REQUESTS_PATH).push().getKey();
                        DonationRequest donationRequest =
                                new DonationRequest(key,
                                        mUid,
                                        users.getUserName(),
                                        array[selectedBloodType[0]],
                                        users.getCity(),
                                        users.getState(),
                                        users.getLocationZip(),
                                        Utils.getCurrentDate()
                                );

                        Map<String, Object> donationValues = donationRequest.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/" + DONATION_REQUESTS_PATH + "/" + key, donationValues);
                        childUpdates.put("/" + MY_DONATION_REQUESTS_PATH + "/" + mUid + "/" + key, donationValues);
                        donationRequestsDBReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Snackbar.make(view, R.string.str_snackbar_submitting_request, Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(view, getString(R.string.str_snackbar_completed_request_sucessfully) + array[selectedBloodType[0]] + " blood type", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.create().show();
            }
        });

        myDonationRequestsAdapter = new MyDonationRequestsAdapter(MyDonationRequestsFragment.this, mUid, mUserName, view.getContext());
        recyclerViewMyDonationsRequests.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL, false));
        recyclerViewMyDonationsRequests.setAdapter(myDonationRequestsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewMyDonationsRequests.addItemDecoration(dividerItemDecoration);
        toggleRecyclerView();
        return view;
    }

    private void attachDatabaseReadListener() {
        if (userValueEventListener == null) {
            userValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users = dataSnapshot.getValue(Users.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            userDBReference.addListenerForSingleValueEvent(userValueEventListener);
        }

        if (myDonationRequestChildEventListener == null) {
            myDonationRequestChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DonationRequest donationRequest = dataSnapshot.getValue(DonationRequest.class);
                    myDonationRequestList.add(donationRequest);
                    myDonationRequestsAdapter.prepareMyDonationRequestList(myDonationRequestList);

                    editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(myDonationRequestList);
                    editor.putString(MY_DONATION_REQUEST_SHARED_PREF_KEY, json);
                    editor.apply();
                    sendBroadcast();

                    toggleRecyclerView();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    if (myDonationRequestList.size() > 0) myDonationRequestList.clear();
                    DonationRequest donationRequest = dataSnapshot.getValue(DonationRequest.class);
                    myDonationRequestList.add(donationRequest);
                    myDonationRequestsAdapter.prepareMyDonationRequestList(myDonationRequestList);

                    editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(myDonationRequestList);
                    editor.putString(MY_DONATION_REQUEST_SHARED_PREF_KEY, json);
                    editor.apply();
                    sendBroadcast();

                    toggleRecyclerView();
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
            myDonationRequestDBReference.addChildEventListener(myDonationRequestChildEventListener);
        }
    }

    private void toggleRecyclerView() {
        if (myDonationRequestList.size() > 0) {
            recyclerViewMyDonationsRequests.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            recyclerViewMyDonationsRequests.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void detachDatabaseReadListener() {
        if (userValueEventListener != null) {
            userDBReference.removeEventListener(userValueEventListener);
            userValueEventListener = null;
        }
        if (myDonationRequestChildEventListener != null) {
            myDonationRequestDBReference.removeEventListener(myDonationRequestChildEventListener);
            myDonationRequestChildEventListener = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        myDonationRequestList.clear();
        detachDatabaseReadListener();
    }

    @Override
    public void onClick(DonationRequest donationRequest, String mUid, String mUserName) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        intent.putExtra(MY_DONATION_REQUEST_DATA, donationRequest);
        intent.putExtra(USER_ID, mUid);
        intent.putExtra(USERNAME, mUserName);
        startActivity(intent);
    }

    private void sendBroadcast() {
        Intent intent = new Intent(view.getContext(), DonorAppWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE\"");
        view.getContext().sendBroadcast(intent);
    }
}
