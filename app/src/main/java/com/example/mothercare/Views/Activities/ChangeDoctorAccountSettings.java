package com.example.mothercare.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Doctor;
import com.example.mothercare.Models.UserLocation;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

public class ChangeDoctorAccountSettings extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private FirebaseUtil firebaseUtil;
    private Doctor doctor;
    private EditText name, phoneNumber, qualification, specialization, worksAt, institute, charges;
    private ImageView pickLocation;
    private int PLACE_PICKER_REQUEST = 1;
    private UserLocation userLocation;
    private Button saveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        getSupportActionBar().setTitle("Change Doctor Account Settings");
        init();
        showHideProgress(true, "");
        firebaseUtil.getDoctorInfo(firebaseUtil.getCurrentUserID());
    }

    private void init() {
        name = findViewById(R.id.userName);
        phoneNumber = findViewById(R.id.phoneNumber);
        qualification = findViewById(R.id.qualification);
        specialization = findViewById(R.id.specialization);
        institute = findViewById(R.id.institute);
        worksAt = findViewById(R.id.worksAt);
        charges = findViewById(R.id.charges);
        pickLocation = findViewById(R.id.locationImageView);
        saveChanges = findViewById(R.id.saveChanges);
        MapUtility.apiKey = getResources().getString(R.string.places_api_key);
        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangeDoctorAccountSettings.this, LocationPickerActivity.class);
                startActivityForResult(i, PLACE_PICKER_REQUEST);
            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setFocusable(true);
                    name.setError("Name cannot be empty! ");
                    return;
                } else if (phoneNumber.getText().toString().isEmpty()) {
                    phoneNumber.setFocusable(true);
                    phoneNumber.setError("Phone number cannot be empty! ");
                    return;
                } else if (qualification.getText().toString().isEmpty()) {
                    qualification.setFocusable(true);
                    qualification.setError("Qualification cannot be empty! ");
                    return;
                } else if (specialization.getText().toString().isEmpty()) {
                    specialization.setFocusable(true);
                    specialization.setError("Experience cannot be empty! ");
                    return;
                } else if (institute.getText().toString().isEmpty()) {
                    institute.setFocusable(true);
                    institute.setError("Educational Institute cannot be empty! ");
                    return;
                } else if (worksAt.getText().toString().isEmpty()) {
                    worksAt.setFocusable(true);
                    worksAt.setError("Working place cannot be empty! ");
                    return;
                } else if (charges.getText().toString().isEmpty()) {
                    charges.setFocusable(true);
                    charges.setError("Charges cannot be empty! ");
                    return;
                } else {
                    doctor.username = name.getText().toString();
                    doctor.phoneNumber = phoneNumber.getText().toString();
                    doctor.experience = Integer.parseInt(specialization.getText().toString());
                    doctor.qualification = qualification.getText().toString();
                    doctor.institute = institute.getText().toString();
                    doctor.worksat = worksAt.getText().toString();
                    if (userLocation != null)
                        doctor.setUserLocation(userLocation);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors").child(doctor.doctorID);
                    reference.setValue(doctor);
                    showToast("Data Saved Successfully");
                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_change_doctor_account_settings;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case SpecificDoctorInformation: {
                showHideProgress(false, "");
                doctor = (Doctor) o;
                name.setText(doctor.username);
                phoneNumber.setText(doctor.phoneNumber);
                qualification.setText(doctor.qualification);
                specialization.setText(String.valueOf(doctor.experience));
                institute.setText(doctor.institute);
                worksAt.setText(doctor.worksat);
                charges.setText(String.valueOf(doctor.charges));
                break;
            }
            case Error: {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    double selectedLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double selectedLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    userLocation = new UserLocation(selectedLatitude, selectedLongitude);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}
