package com.android.mvnshrikanth.theblooddonor.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.mvnshrikanth.theblooddonor.R;
import com.android.mvnshrikanth.theblooddonor.data.Location;
import com.android.mvnshrikanth.theblooddonor.data.Users;
import com.android.mvnshrikanth.theblooddonor.utilities.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

//TODO 2) Add location api for getting autofill of location in the form.

    public static final String NEW_USER = "new_user";
    public static final String USERNAME = "username";
    public static final String USER_ID = "user_id";
    private static final int RC_PHOTO_PICKER = 2;

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
    @BindView(R.id.imageButton_profile_picture)
    ImageButton imageButtonProfilePicture;

    private DatabaseReference usersDatabaseReference;
    private ChildEventListener userChildEventListener;
    private ValueEventListener userValueEvenListener;
    private StorageReference userPhotosStorageReference;
    private FirebaseStorage firebaseStorage;
    private String mUserName;
    private String mUid;
    private Users user;
    private Boolean newUser;
    private String userPhotoUrl;
    private Location locationData;

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
        firebaseStorage = FirebaseStorage.getInstance();
        usersDatabaseReference = firebaseDatabase.getReference().child("users").child(mUid);
        userPhotosStorageReference = firebaseStorage.getReference().child("chat_photos");

        ArrayAdapter<CharSequence> adapterGender =
                ArrayAdapter.createFromResource(this, R.array.sex_array, R.layout.support_simple_spinner_dropdown_item);
        adapterGender.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapterGender);

        ArrayAdapter<CharSequence> adapterBloodType =
                ArrayAdapter.createFromResource(this, R.array.blood_type_array, R.layout.support_simple_spinner_dropdown_item);
        adapterBloodType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_blood_type.setAdapter(adapterBloodType);
        userPhotoUrl = null;
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
                    user.setPhotoUrl(userPhotoUrl);
                } else {
                    user = new Users(
                            mUserName,
                            spinner_blood_type.getSelectedItem().toString().trim(),
                            editTextZipCode.getText().toString().trim(),
                            editTextCity.getText().toString(),
                            editTextState.getText().toString(),
                            editTextCountry.getText().toString(),
                            spinner_gender.getSelectedItem().toString(),
                            userPhotoUrl);
                }
                usersDatabaseReference.setValue(user);
                finish();
            }
        });

        editTextZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 5) {
                    loadLocationInfo(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imageButtonProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }

    private void loadLocationInfo(String zipCode) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Utils.ZIP_CODE_API_BASE_URL + "/info.json/" + zipCode + "/radians";
        locationData = null;

        final Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                final String responseData = response.body().string();

                if (response.isSuccessful()) {
                    try {
                        locationData = Utils.getCityStateFromJSONString(responseData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLocation(locationData);
                    }
                });
            }
        });
    }

    private void showLocation(Location location) {
        if (location == null) {
            editTextCity.setText(" ");
            editTextState.setText(" ");
            editTextCountry.setText(" ");
        } else {
            editTextCity.setText(location.getCity());
            editTextState.setText(location.getState());
            editTextCountry.setText("USA");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            Uri selectedImageUri = data.getData();
            // Get a reference to store file at chat_photos/<FILENAME>
            assert selectedImageUri != null;
            StorageReference photoRef = userPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            userPhotoUrl = downloadUrl.toString();
                            if (user == null) {
                                user = new Users(
                                        mUserName,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        userPhotoUrl);
                            } else {
                                user.setPhotoUrl(userPhotoUrl);
                            }

                            Glide.with(getApplicationContext())
                                    .load(userPhotoUrl)
                                    .into(imageButtonProfilePicture);
                        }
                    });
        }
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
                    userPhotoUrl = user.getPhotoUrl();
                    if (user.getPhotoUrl() != null) {
                        Glide.with(getApplicationContext())
                                .load(userPhotoUrl)
                                .into(imageButtonProfilePicture);
                    }
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
