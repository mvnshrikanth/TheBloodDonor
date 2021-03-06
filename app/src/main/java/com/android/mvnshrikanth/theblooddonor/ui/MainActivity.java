package com.android.mvnshrikanth.theblooddonor.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.mvnshrikanth.theblooddonor.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.NEW_USER;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USERNAME;
import static com.android.mvnshrikanth.theblooddonor.ui.ProfileActivity.USER_ID;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference usersDatabaseReference;
    private ValueEventListener userValueEventListener;


    private String mUsername;
    private String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!networkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.str_network_connection_not_available)
                    .setTitle(R.string.str_no_connection_alert_title)
                    .create()
                    .show();
        } else {

            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            usersDatabaseReference = firebaseDatabase.getReference().child("users");

            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        onSignedInInitialize(user.getDisplayName(), user.getUid());
                    } else {
                        onSignedOutCleanup();
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setIsSmartLockEnabled(false)
                                        .setAvailableProviders(
                                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                        .build(),
                                RC_SIGN_IN);
                    }
                }
            };
        }
    }

    public boolean networkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    private void onSignedInInitialize(String displayName, final String uid) {
        mUsername = displayName;
        mUid = uid;

        attachDatabaseReadListener();
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, mUid);
        bundle.putString(USERNAME, mUsername);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_container, mainFragment)
                .commit();
    }

    private void onSignedOutCleanup() {
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {

        userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(mUid)) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra(NEW_USER, true);
                    intent.putExtra(USER_ID, mUid);
                    intent.putExtra(USERNAME, mUsername);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        usersDatabaseReference.addListenerForSingleValueEvent(userValueEventListener);
    }

    private void detachDatabaseReadListener() {

        if (userValueEventListener != null) {
            usersDatabaseReference.removeEventListener(userValueEventListener);
            userValueEventListener = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        detachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkAvailable()) {
            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (networkAvailable()) {
            switch (item.getItemId()) {
                case R.id.item_show_profile:
                    Intent intent = new Intent(this, ProfileActivity.class);
                    intent.putExtra(NEW_USER, false);
                    intent.putExtra(USER_ID, mUid);
                    intent.putExtra(USERNAME, mUsername);
                    startActivity(intent);
                    break;
                default:
                    AuthUI.getInstance().signOut(this);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.str_network_connection_not_available)
                    .setTitle(R.string.str_no_connection_alert_title)
                    .create()
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}
