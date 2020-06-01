package com.example.mothercare.Views.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.AppointmentRequestAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Models.AppointmentRequest;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAppointmentRequests extends BaseActivity {
    private RecyclerView appointmentRequestsRecyclerView;
    ArrayList<AppointmentRequest> appointmentRequestArrayList = new ArrayList<>();
    ArrayList<Patient> patientsArrayList = new ArrayList<>();
    private TextView noAppointmentRequestRecyclerView;
    private AppointmentRequestAdapter adapter;
    private FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseUtil = new FirebaseUtil(this);
        appointmentRequestsRecyclerView = findViewById(R.id.appointmentRequestsRecyclerView);
        noAppointmentRequestRecyclerView = findViewById(R.id.noAppointmentRequest);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        appointmentRequestsRecyclerView.setLayoutManager(mLayoutManager);
        appointmentRequestsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AppointmentRequestAdapter(ViewAppointmentRequests.this, appointmentRequestArrayList, patientsArrayList);
        appointmentRequestsRecyclerView.setAdapter(adapter);
        getAppointmentHistory();
    }

    public void getAppointmentHistory() {
        showHideProgress(true, "");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("AppointmentRequest");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AppointmentRequest appointmentRequest = postSnapshot.getValue(AppointmentRequest.class);
                    if (appointmentRequest.doctorID.equals(firebaseUtil.getCurrentUserID())) {
                        appointmentRequestArrayList.add(appointmentRequest);
                    }
                    if (appointmentRequestsRecyclerView.getVisibility() == View.GONE) {
                        appointmentRequestsRecyclerView.setVisibility(View.VISIBLE);
                        noAppointmentRequestRecyclerView.setVisibility(View.GONE);
                    }
                }
                if (appointmentRequestArrayList.size() == 0) {
                    appointmentRequestsRecyclerView.setVisibility(View.GONE);
                    noAppointmentRequestRecyclerView.setVisibility(View.VISIBLE);
                }
                getPatients();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showHideProgress(false, "");
            }
        });
    }

    public void getPatients() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patient");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (AppointmentRequest appointmentRequest : appointmentRequestArrayList) {
                        Patient patient = snapshot.getValue(Patient.class);
                        if (patient.patientID.equals(appointmentRequest.patientID)) {
                            patientsArrayList.add(patient);
                        }
                    }
                }
                showHideProgress(false, "");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_view_appointment_requests;
    }
}
