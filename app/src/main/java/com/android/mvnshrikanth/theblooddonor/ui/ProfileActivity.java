package com.android.mvnshrikanth.theblooddonor.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.mvnshrikanth.theblooddonor.utilities.Utils.USER_PROFILE_PICTURES_STORAGE_PATH;

public class ProfileActivity extends AppCompatActivity {

    public static final String NEW_USER = "new_user";
    public static final String USERNAME = "username";
    public static final String USER_ID = "user_id";
    private static final int RC_PHOTO_PICKER = 2;
    private static final String LOG_TAG = ProfileActivity.class.getSimpleName();

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
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolBarUserName;

    private DatabaseReference usersDatabaseReference;
    private ChildEventListener userChildEventListener;
    private StorageReference userPhotosStorageReference;
    private String mUserName;
    private String mUid;
    private Users user;
    private String userPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Boolean newUser = intent.getBooleanExtra(NEW_USER, false);
        mUserName = intent.getStringExtra(USERNAME);
        mUid = intent.getStringExtra(USER_ID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        usersDatabaseReference = firebaseDatabase.getReference().child("users").child(mUid);
        userPhotosStorageReference = firebaseStorage.getReference().child(USER_PROFILE_PICTURES_STORAGE_PATH);

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

                if (verifyUserInputs()) {
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
            }
        });

        editTextZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 5) {
//                    loadLocationInfo(charSequence.toString().trim());
                    new LocationAsyncTask().execute(charSequence.toString().trim());
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

    @NonNull
    private Boolean verifyUserInputs() {
        String toastMessage = "";
        if (spinner_blood_type.getSelectedItem().toString().trim().length() == 0) {
            toastMessage = getString(R.string.str_toast_blood_type);
        }
        if (editTextZipCode.getText().toString().trim().length() != 5) {
            toastMessage = toastMessage + ", " + getString(R.string.str_toast_zip_code);
        }
        if (editTextCity.getText().toString().length() == 0) {
            toastMessage = toastMessage + ", " + getString(R.string.str_toast_city);
        }
        if (editTextState.getText().toString().length() == 0) {
            toastMessage = toastMessage + ", " + getString(R.string.str_toast_state);
        }
        if (editTextCountry.getText().toString().length() == 0) {
            toastMessage = toastMessage + ", " + getString(R.string.str_country_toast);
        }
        if (spinner_gender.getSelectedItem().toString().length() == 0) {
            toastMessage = toastMessage + ", " + getString(R.string.str_toast_gender);
        }
        if (toastMessage.length() > 0) {
            Toast.makeText(ProfileActivity.this, getString(R.string.str_toast_message_user_input_validation) + toastMessage, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showLocation(Location location) {
        if (location == null) {
            editTextCity.setText("");
            editTextState.setText("");
            editTextCountry.setText("");
        } else {
            editTextCity.setText(location.getCity());
            editTextState.setText(location.getState());
            editTextCountry.setText(R.string.str_default_country_name);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            // Get a reference to store file at user_photos/<FILENAME>
            assert selectedImageUri != null;
            StorageReference photoRef = userPhotosStorageReference.child(mUid);

            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            assert downloadUrl != null;
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

        ValueEventListener userValueEvenListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Users.class);
                collapsingToolBarUserName.setTitle(mUserName);
                if ((user != null) && (user.getUserName().equals(mUserName))) {
                    loadProfileActivityUI(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersDatabaseReference.addValueEventListener(userValueEvenListener);
    }

    private void loadProfileActivityUI(Users users) {

        editTextCity.setText(users.getCity());
        editTextState.setText(users.getState());
        editTextCountry.setText(users.getCountry());
        editTextZipCode.setText(users.getLocationZip());
        spinner_gender.setSelection(((ArrayAdapter) spinner_gender.getAdapter()).getPosition(users.getGender()));
        spinner_blood_type.setSelection(((ArrayAdapter) spinner_blood_type.getAdapter()).getPosition(users.getBloodType()));
        userPhotoUrl = users.getPhotoUrl();
        if (users.getPhotoUrl() != null) {
            Glide.with(getApplicationContext())
                    .load(userPhotoUrl)
                    .into(imageButtonProfilePicture);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userChildEventListener != null) {
            usersDatabaseReference.removeEventListener(userChildEventListener);
            userChildEventListener = null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LocationAsyncTask extends AsyncTask<String, Void, Location> {

        @Override
        protected Location doInBackground(String... strings) {
            String strUrl = Utils.ZIP_CODE_API_BASE_URL + "/info.json/" + strings[0] + "/radians";
            Location location = null;
            try {
                URL url = new URL(strUrl);
                String responseData = Utils.getResponseFromHttpUrl(url);
                location = Utils.getCityStateFromJSONString(responseData);
            } catch (IOException | JSONException e) {
                Log.e(LOG_TAG, "Error in retrieving data." + e.getMessage());
            }

            return location;
        }

        @Override
        protected void onPostExecute(Location location) {
            super.onPostExecute(location);
            showLocation(location);
        }
    }
}
