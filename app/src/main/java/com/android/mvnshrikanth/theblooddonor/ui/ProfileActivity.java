package com.android.mvnshrikanth.theblooddonor.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

//TODO 1) Add location api for getting autfill of location in the form.

    public static final String NEW_USER = "NEW_USER";
    public static final String USERNAME = "USERNAME";
    public static final String USER_ID = "USER_ID";

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
    @BindView(R.id.editText_city)
    EditText editTextCity;
    @BindView(R.id.editText_country)
    EditText editTextCountry;
    @BindView(R.id.editText_state)
    EditText editTextState;

    @BindView(R.id.textView_name)
    TextView textViewName;
    @BindView(R.id.textView_gender)
    TextView textViewGender;
    @BindView(R.id.textView_blood_type)
    TextView textViewBloodGroup;
    @BindView(R.id.textView_zip)
    TextView textViewZip;
    @BindView(R.id.textView_city)
    TextView textViewCity;
    @BindView(R.id.textView_state)
    TextView textViewState;
    @BindView(R.id.textView_country)
    TextView textViewCountry;

    private DatabaseReference usersDatabaseReference;
    private ChildEventListener userChildEventListener;
    private String mUserName;
    private String mUid;
    private Users user;
    private Boolean newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        newUser = intent.getBooleanExtra(NEW_USER, false);
        mUserName = intent.getStringExtra(USERNAME);
        mUid = intent.getStringExtra(USER_ID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        usersDatabaseReference = firebaseDatabase.getReference().child("users");

        if (newUser) {
            showEditableCardView(true);
        } else {
            showEditableCardView(false);
        }

        onSignedInInitialize();

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
                showEditableCardView(true);

                if (user != null) {
                    editTextZipCode.setText(user.getLocationZip());
                    editTextCity.setText(user.getCity());
                    editTextState.setText(user.getState());
                    editTextCountry.setText(user.getCountry());
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
                    user.setCity(editTextCity.getText().toString());
                    user.setState(editTextState.getText().toString());
                    user.setCountry(editTextCountry.getText().toString());
                    user.setGender(spinner_gender.getSelectedItem().toString());
                    user.setPhotoUrl(null);
                } else {
                    user = new Users(
                            mUserName,
                            spinner_blood_type.getSelectedItem().toString(),
                            editTextZipCode.getText().toString(),
                            editTextCity.getText().toString(),
                            editTextState.getText().toString(),
                            editTextCountry.getText().toString(),
                            spinner_gender.getSelectedItem().toString(),
                            null);
                }
                usersDatabaseReference.child(mUid).setValue(user);

            }
        });
    }

    private void onSignedInInitialize() {


        userChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                user = dataSnapshot.getValue(Users.class);


                if ((user != null) && (user.getUserName().equals(mUserName))) {
                    textViewName.setText(mUserName);
                    textViewBloodGroup.setText(user.getBloodType());
                    textViewGender.setText(user.getGender());
                    textViewZip.setText(user.getLocationZip());
                    textViewCity.setText(user.getCity());
                    textViewState.setText(user.getState());
                    textViewCountry.setText(user.getCountry());

                    showEditableCardView(false);
                } else {
                    showEditableCardView(true);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                user = dataSnapshot.getValue(Users.class);

                if ((user != null) && (user.getUserName().equals(mUserName))) {
                    textViewName.setText(mUserName);
                    textViewBloodGroup.setText(user.getBloodType());
                    textViewGender.setText(user.getGender());
                    textViewZip.setText(user.getLocationZip());
                    textViewCity.setText(user.getCity());
                    textViewState.setText(user.getState());
                    textViewCountry.setText(user.getCountry());

                    showEditableCardView(false);
                }
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

        usersDatabaseReference.addChildEventListener(userChildEventListener);
    }

    private void showEditableCardView(Boolean show) {

        if (show) {
            cardView_editable.setVisibility(View.VISIBLE);
            cardView_view_only.setVisibility(View.GONE);
        } else {
            cardView_editable.setVisibility(View.GONE);
            cardView_view_only.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userChildEventListener != null) {
            usersDatabaseReference.removeEventListener(userChildEventListener);
            userChildEventListener = null;
        }
    }
}
