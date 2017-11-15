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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.mvnshrikanth.theblooddonor.ui.MainActivity.RC_SIGN_IN;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.spinner_gender)
    Spinner spinner_sex;
    @BindView(R.id.spinner_blood_type)
    Spinner spinner_blood_type;
    @BindView(R.id.cardView_view_only)
    CardView cardView_view_only;
    @BindView(R.id.cardView_editable)
    CardView cardView_editable;
    @BindView(R.id.linearLayout_scrollView_container)
    LinearLayout linearLayout_scrollView_container;
    @BindView(R.id.button_save)
    Button button_save;
    @BindView(R.id.editText_zip)
    EditText editTextZipCode;
    @BindView(R.id.textView_name)
    TextView textViewName;
    @BindView(R.id.textView_gender)
    TextView textViewGender;
    @BindView(R.id.textView_blood_group)


    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDatabaseReference;
    private String mUserName;
    private String mUid;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
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
        spinner_sex.setAdapter(adapterGender);

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
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(linearLayout_scrollView_container);

                user = new Users(
                        mUserName,
                        spinner_blood_type.getSelectedItem().toString(),
                        editTextZipCode.getText().toString(),
                        spinner_sex.getSelectedItem().toString(),
                        null);

                usersDatabaseReference.child(mUid).setValue(user);

                cardView_editable.setVisibility(View.GONE);
                cardView_view_only.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onSignedInInitialize(String displayName, String uid) {
        mUid = uid;
        mUserName = displayName;
        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mUid)) {
                    cardView_editable.setVisibility(View.GONE);
                    cardView_view_only.setVisibility(View.VISIBLE);
                    user = dataSnapshot.getValue(Users.class);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
