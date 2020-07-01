package com.example.mothercare.Views.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfileActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private String patientID;
    private FirebaseUtil firebaseUtil;
    private CircleImageView profilePictureIV;
    private TextView name, trimester, month, week, phoneNumber, email;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        patientID = intent.getStringExtra("ID");
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getPatientInfo(patientID);

        //Init
        profilePictureIV = findViewById(R.id.profilePicture);
        name = findViewById(R.id.name);
        trimester = findViewById(R.id.trimester);
        month = findViewById(R.id.month);
        week = findViewById(R.id.week);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.patient_profile;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case SpecificPatientInformation: {
                patient = (Patient) o;
                firebaseUtil.getProfilePicture("Patient", patientID);
                break;
            }
            case ProfilePicture: {
                profilePictureIV.setImageBitmap((Bitmap) o);
                name.setText(patient.username);
                trimester.setText(String.valueOf(patient.trimester));
                month.setText(String.valueOf(patient.pregnancyMonth));
                week.setText(String.valueOf(patient.pregnancyWeek));
                phoneNumber.setText(patient.phoneNumber);
                email.setText(patient.email);
            }
            case Error: {
                break;
            }
        }
    }
}