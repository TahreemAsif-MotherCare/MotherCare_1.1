package com.example.mothercare.Views.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.List;

public class PatientProfile extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private Patient patient;
    private TextView patientName, contactNumber, email, trimester, week, month;
    private ImageView chatWithPatient, call, sendEmail, patientProfilePicture;
    private FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        init();
        Intent intent = getIntent();
        firebaseUtil.getPatientInfo(intent.getStringExtra("PatientID"));
    }

    private void init() {
        patientName = findViewById(R.id.patientName);
        contactNumber = findViewById(R.id.contactNumber);
        email = findViewById(R.id.email);
        trimester = findViewById(R.id.trimester);
        week = findViewById(R.id.week);
        month = findViewById(R.id.month);
        chatWithPatient = findViewById(R.id.chatWithPatient);
        call = findViewById(R.id.call);
        sendEmail = findViewById(R.id.sendEmail);
        patientProfilePicture = findViewById(R.id.patientProfilePicture);

        chatWithPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, DoctorChatActivity.class);
                intent.putExtra("PatientID", patient.patientID);
                startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + patient.phoneNumber));
                startActivity(callIntent);
            }
        });
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{patient.email});
                emailIntent.setType("text/plain");
                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(emailIntent);
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_patient_profile;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case ProfilePicture: {
                showHideProgress(false, "");
                patientProfilePicture.setImageBitmap((Bitmap) o);
            }
            case SpecificPatientInformation: {
                patient = (Patient) o;
                patientName.setText(patient.username);
                contactNumber.setText(patient.phoneNumber);
                email.setText(patient.email);
                try {
                    trimester.setText(patient.trimester);
                    month.setText(patient.pregnancyMonth);
                    week.setText(patient.pregnancyWeek);
                } catch (Exception e) {
                    trimester.setText("Not available at the moment!");
                    month.setText("Not available at the moment!");
                    week.setText("Not available at the moment!");
                } finally {
                    firebaseUtil.getProfilePicture("Patient", patient.patientID);
                }
            }
            case Error: {
                Log.d("Error", "firebaseResponse: " + o.toString());
            }
        }
    }
}
