package com.example.mothercare.Views.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePatientAccountSettingsActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private static final String TAG = "Error";
    private EditText name, phoneNumber, email, trimester, month, week;
    private Button saveChanges;
    private Patient patient;
    private FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getSupportActionBar().setTitle("Change Patient Account Settings");
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getPatientInfo(firebaseUtil.getCurrentUserID());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_change_patient_account_settings;
    }

    private void init() {
        name = findViewById(R.id.userName);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.signupEmail);
        trimester = findViewById(R.id.trimester);
        month = findViewById(R.id.month);
        week = findViewById(R.id.week);
        saveChanges = findViewById(R.id.saveChanges);

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
                } else if (email.getText().toString().isEmpty()) {
                    email.setFocusable(true);
                    email.setError("Email cannot be empty! ");
                    return;
                } else if (trimester.getText().toString().isEmpty()) {
                    trimester.setFocusable(true);
                    trimester.setError("Trimester cannot be empty! ");
                    return;
                } else if (month.getText().toString().isEmpty()) {
                    month.setFocusable(true);
                    month.setError("Month cannot be empty! ");
                    return;
                } else if (week.getText().toString().isEmpty()) {
                    week.setFocusable(true);
                    week.setError("Week cannot be empty! ");
                    return;
                } else {
                    Patient updatePatient = new Patient(patient.patientID, name.getText().toString(), patient.email, phoneNumber.getText().toString()
                            , Integer.parseInt(trimester.getText().toString()), Integer.parseInt(month.getText().toString()), Integer.parseInt(week.getText().toString()));
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patient").child(patient.patientID);
                    reference.setValue(updatePatient);

                    if (!email.getText().toString().equals(patient.email)) {
                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User email address updated.");
                                            Toast.makeText(ChangePatientAccountSettingsActivity.this, "The email updated.", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }
                    showToast("Data Saved Successfully");
                }
            }
        });
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case SpecificPatientInformation: {
                patient = (Patient) o;
                name.setText(patient.username);
                email.setText(patient.email);
                phoneNumber.setText(patient.phoneNumber);
                try {
                    trimester.setText(String.valueOf(patient.trimester));
                    month.setText(String.valueOf(patient.pregnancyMonth));
                    week.setText(String.valueOf(patient.pregnancyWeek));
                } catch (Exception e) {
                    trimester.setText("0");
                    month.setText("0");
                    week.setText("0");
                }
            }
            case Error: {
                Log.d(TAG, "firebaseResponse: " + o.toString());
            }
        }
    }
}
