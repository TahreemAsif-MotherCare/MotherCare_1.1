package com.example.mothercare.Views.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.DoctorProfileAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Doctor;
import com.example.mothercare.Models.DoctorProfile;
import com.example.mothercare.Models.Request;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class DoctorProfileActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private Doctor doctor;
    TextView username;
    ImageView profilePic;
    RecyclerView recyclerView;
    ImageView chatWithDoctor, sendRequest;
    private FirebaseUtil firebaseUtil;
    TextView specialization, qualification, education, worksat, doctorCharges;
    DoctorProfileAdapter doctorProfileAdapter;
    ArrayList<DoctorProfile> doctorProfileActivityArrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Objects.requireNonNull(getSupportActionBar()).hide();
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        init();
        showHideProgress(true, "");
        firebaseUtil.getDoctorInfo(intent.getStringExtra("doctorID"));

    }

    private void init() {
        username = findViewById(R.id.userNameTextView);
        specialization = findViewById(R.id.specializationDP);
        qualification = findViewById(R.id.qualificationDP);
        education = findViewById(R.id.educationDP);
        worksat = findViewById(R.id.worksAtTextViewDP);
        profilePic = findViewById(R.id.userImage);
        sendRequest = findViewById(R.id.sendRequestButton);
        chatWithDoctor = findViewById(R.id.chatWithDoctor);
        doctorCharges = findViewById(R.id.feesTextViewDP);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
        chatWithDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((sendRequest.getDrawable() != getResources().getDrawable(R.drawable.ic_done_white_24dp)) && sendRequest.isEnabled()) {
                    showToast("Please Add Doctor First");
                } else if ((sendRequest.getDrawable() == getResources().getDrawable(R.drawable.ic_done_white_24dp)) && (!sendRequest.isEnabled())) {
                    showToast("Your doctor hasn't accepted your request yet");
                } else {
                    Intent intent = new Intent(DoctorProfileActivity.this, PatientChatActivity.class);
                    intent.putExtra("DoctorID", doctor.doctorID);
                    startActivity(intent);
                }
            }
        });
    }

    public void ifRequestSent() {
        showHideProgress(true, "");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()
                + "__" + doctor.doctorID);
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        sendRequest.setEnabled(false);
                        sendRequest.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
                        showHideProgress(false, "");
                    } else {
                        ifAdded();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            showHideProgress(false, "");
            showAlertDialog("Error", e.getLocalizedMessage());
        }
    }

    public void ifAdded() {
        showHideProgress(true, "");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("AddedDoctors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()
                + "__" + doctor.doctorID);
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        sendRequest.setEnabled(false);
                        sendRequest.setImageDrawable(getResources().getDrawable(R.drawable.ic_added_friends));
                        showHideProgress(false, "");
                    } else {
                        showHideProgress(false, "");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
            showHideProgress(false, "");
            showAlertDialog("Error", e.getLocalizedMessage());
        }
    }

    private void sendRequest() {
        DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference().child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid() +
                "__" + doctor.doctorID);
        Request request = new Request(FirebaseAuth.getInstance().getCurrentUser().getUid(), doctor.doctorID);
        doctorRef.setValue(request);
        sendRequest.setEnabled(false);
    }

    private void prepareDoctorProfileData(Doctor doctor) {
        DoctorProfile doctorProfile = new DoctorProfile("Specialization", String.valueOf(doctor.experience));
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Qualification", doctor.qualification);
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Studied From", doctor.institute);
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Works At", doctor.worksat);
        doctorProfileActivityArrayList.add(doctorProfile);

        if (doctor.mondayAvailable)
            doctorProfile = new DoctorProfile("Available on Monday", "Yes");
        else
            doctorProfile = new DoctorProfile("Available on Monday", "No");
        doctorProfileActivityArrayList.add(doctorProfile);

        if (doctor.tuesdayAvailable)
            doctorProfile = new DoctorProfile("Available on Tuesday", "Yes");
        else
            doctorProfile = new DoctorProfile("Available on Tuesday", "No");
        doctorProfileActivityArrayList.add(doctorProfile);

        if (doctor.wednesdayAvailable)
            doctorProfile = new DoctorProfile("Available on Wednesday", "Yes");
        else
            doctorProfile = new DoctorProfile("Available on Wednesday", "No");
        doctorProfileActivityArrayList.add(doctorProfile);

        if (doctor.thursdayAvailable)
            doctorProfile = new DoctorProfile("Available on Thursday", "Yes");
        else
            doctorProfile = new DoctorProfile("Available on Thursday", "No");
        doctorProfileActivityArrayList.add(doctorProfile);

        if (doctor.fridayAvailable)
            doctorProfile = new DoctorProfile("Available on Friday", "Yes");
        else
            doctorProfile = new DoctorProfile("Available on Friday", "No");
        doctorProfileActivityArrayList.add(doctorProfile);

        if (doctor.saturdayAvailable)
            doctorProfile = new DoctorProfile("Available on Saturday", "Yes");
        else
            doctorProfile = new DoctorProfile("Available on Saturday", "No");
        doctorProfileActivityArrayList.add(doctorProfile);

        if (doctor.sundayAvailable)
            doctorProfile = new DoctorProfile("Available on Sunday", "Yes");
        else
            doctorProfile = new DoctorProfile("Available on Sunday", "No");
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Monday From:", doctor.getMondayStartTime());
        doctorProfileActivityArrayList.add(doctorProfile);
        doctorProfile = new DoctorProfile("Available on Monday To:", doctor.getMondayEndTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Tuesday From:", doctor.getTuesdayStartTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Tuesday To:", doctor.getTuesdayEndTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Wednesday From:", doctor.getWedStartTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Wednesday To:", doctor.getWedEndTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Thursday From:", doctor.getThuStartTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Thursday To:", doctor.getThuEndTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Friday From:", doctor.getFriStartTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Friday To:", doctor.getFriEndTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Saturday From:", doctor.getSatStartTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Saturday To:", doctor.getSatEndTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Sunday From:", doctor.getSunStartTime());
        doctorProfileActivityArrayList.add(doctorProfile);

        doctorProfile = new DoctorProfile("Available on Sunday To:", doctor.getSunEndTime());
        doctorProfileActivityArrayList.add(doctorProfile);


        doctorProfileAdapter.notifyDataSetChanged();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_doctor_profile;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case SpecificDoctorInformation: {
                doctor = (Doctor) o;
                username.setText(doctor.username);
                specialization.setText(String.valueOf(doctor.experience));
                education.setText(doctor.institute);
                qualification.setText(doctor.qualification);
                worksat.setText(doctor.worksat);
                try {
                    doctorCharges.setText(String.valueOf(doctor.charges));
                } catch (NullPointerException e) {
                    doctorCharges.setText("N/A");
                }
                firebaseUtil.getProfilePicture("Doctor", doctor.doctorID);
                break;
            }
            case ProfilePicture: {
                showHideProgress(false, "");
                profilePic.setImageBitmap((Bitmap) o);
                ifRequestSent();
                break;
            }
            case Error: {
                showHideProgress(false, "");
            }
        }
    }
}
