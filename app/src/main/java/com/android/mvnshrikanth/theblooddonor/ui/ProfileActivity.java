package com.android.mvnshrikanth.theblooddonor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.Users;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

//TODO 2) Add location api for getting autofill of location in the form.

    public static final String NEW_USER = "new_user";
    public static final String USERNAME = "username";
    public static final String USER_ID = "user_id";

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

    private DatabaseReference usersDatabaseReference;
    private ChildEventListener userChildEventListener;
    private ValueEventListener userValueEvenListener;
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
        usersDatabaseReference = firebaseDatabase.getReference().child("users").child(mUid);

        ArrayAdapter<CharSequence> adapterGender =
                ArrayAdapter.createFromResource(this, R.array.sex_array, R.layout.support_simple_spinner_dropdown_item);
        adapterGender.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapterGender);

        ArrayAdapter<CharSequence> adapterBloodType =
                ArrayAdapter.createFromResource(this, R.array.blood_type_array, R.layout.support_simple_spinner_dropdown_item);
        adapterBloodType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_blood_type.setAdapter(adapterBloodType);

        onSignedInInitialize();

        button_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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
                usersDatabaseReference.setValue(user);
            }
        });
    }

    private void onSignedInInitialize() {

        userValueEvenListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Users.class);

                if ((user != null) && (user.getUserName().equals(mUserName))) {
                    editTextCity.setText(user.getCity());
                    editTextState.setText(user.getState());
                    editTextCountry.setText(user.getCountry());
                    editTextZipCode.setText(user.getLocationZip());
                    spinner_gender.setSelection(((ArrayAdapter) spinner_gender.getAdapter()).getPosition(user.getGender()));
                    spinner_blood_type.setSelection(((ArrayAdapter) spinner_blood_type.getAdapter()).getPosition(user.getBloodType()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersDatabaseReference.addValueEventListener(userValueEvenListener);
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
