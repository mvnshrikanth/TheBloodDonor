package com.android.mvnshrikanth.theblooddonor.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.Users;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.mvnshrikanth.theblooddonor.ui.MainActivity.RC_SIGN_IN;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.cardView_view_only)
    CardView cardView_view_only;
    @BindView(R.id.cardView_editable)
    CardView cardView_editable;
    @BindView(R.id.linearLayout_scrollView_container)
    LinearLayout linearLayout_scrollView_container;

    @BindView(R.id.button_save)
    Button button_save;

    @BindView(R.id.spinner_gender)
    Spinner spinner_gender;
    @BindView(R.id.spinner_blood_type)
    Spinner spinner_blood_type;
    @BindView(R.id.editText_zip)
    EditText editTextZipCode;

    @BindView(R.id.textView_name)
    TextView textViewName;
    @BindView(R.id.textView_gender)
    TextView textViewGender;
    @BindView(R.id.textView_blood_type)
    TextView textViewBloodGroup;
    @BindView(R.id.textView_zip)
    TextView textViewZip;

    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference usersDatabaseReference;
    private String mUserName;
    private String mUid;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        usersDatabaseReference = firebaseDatabase.getReference().child("users");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize(user.getDisplayName(), user.getUid());
                } else {
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
        ArrayAdapter<CharSequence> adapterGender =
                ArrayAdapter.createFromResource(this, R.array.sex_array, R.layout.support_simple_spinner_dropdown_item);
        adapterGender.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapterGender);

        ArrayAdapter<CharSequence> adapterBloodType =
                ArrayAdapter.createFromResource(this, R.array.blood_type_array, R.layout.support_simple_spinner_dropdown_item);
        adapterBloodType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_blood_type.setAdapter(adapterBloodType);

        cardView_view_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(linearLayout_scrollView_container);
                cardView_editable.setVisibility(View.VISIBLE);
                cardView_view_only.setVisibility(View.GONE);

                if (user != null) {
                    editTextZipCode.setText(user.getLocationZip());
                    spinner_gender.setSelection(((ArrayAdapter) spinner_gender.getAdapter()).getPosition(user.getGender()));
                    spinner_blood_type.setSelection(((ArrayAdapter) spinner_blood_type.getAdapter()).getPosition(user.getBloodType()));
                }

            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(linearLayout_scrollView_container);

                if (user != null) {
                    user.setUserName(mUserName);
                    user.setBloodType(spinner_blood_type.getSelectedItem().toString());
                    user.setLocationZip(editTextZipCode.getText().toString());
                    user.setGender(spinner_gender.getSelectedItem().toString());
                    user.setPhotoUrl(null);
                } else {
                    user = new Users(
                            mUserName,
                            spinner_blood_type.getSelectedItem().toString(),
                            editTextZipCode.getText().toString(),
                            spinner_gender.getSelectedItem().toString(),
                            null);
                }
                usersDatabaseReference.child(mUid).setValue(user);

                cardView_editable.setVisibility(View.GONE);
                cardView_view_only.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onSignedInInitialize(String displayName, String uid) {
        mUid = uid;
        mUserName = displayName;

        usersDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                user = dataSnapshot.getValue(Users.class);

                if (user != null) {
                    textViewName.setText(mUserName);
                    textViewBloodGroup.setText(user.getBloodType());
                    textViewGender.setText(user.getGender());
                    textViewZip.setText(user.getLocationZip());

                    cardView_editable.setVisibility(View.GONE);
                    cardView_view_only.setVisibility(View.VISIBLE);
                } else {
                    cardView_editable.setVisibility(View.VISIBLE);
                    cardView_view_only.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                user = dataSnapshot.getValue(Users.class);

                textViewName.setText(mUserName);
                textViewBloodGroup.setText(user.getBloodType());
                textViewGender.setText(user.getGender());
                textViewZip.setText(user.getLocationZip());
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
        });

//        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChild(mUid)) {
//                    cardView_editable.setVisibility(View.GONE);
//                    cardView_view_only.setVisibility(View.VISIBLE);
//                    user = dataSnapshot.child(mUid).getValue(Users.class);
//
//                    textViewName.setText(mUserName);
//                    textViewBloodGroup.setText(user.getBloodType());
//                    textViewGender.setText(user.getGender());
//                    textViewZip.setText(user.getLocationZip());
//                } else {
//                    cardView_editable.setVisibility(View.VISIBLE);
//                    cardView_view_only.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }


}
