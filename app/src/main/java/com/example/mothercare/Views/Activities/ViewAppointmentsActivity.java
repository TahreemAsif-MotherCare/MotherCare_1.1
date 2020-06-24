package com.example.mothercare.Views.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.ViewAppointmentsAdaptor;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Models.Appointment;
import com.example.mothercare.Models.AppointmentRequest;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.Models.Request;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAppointmentsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private TextView noScheduledAppointments;
    ArrayList<Patient> appointments = new ArrayList<>();
    private List<Appointment> appointmentList = new ArrayList<>();
    private ViewAppointmentsAdaptor adapter;
    private FirebaseUtil firebaseUtil;
    private FloatingActionButton requestAppointment;
    private String addedDoctorID = "";
    private String user = "";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.viewAppointmentsRecyclerView);
        noScheduledAppointments = findViewById(R.id.noScheduledAppointment);
        requestAppointment = findViewById(R.id.requestAppointment);
        Intent intent = getIntent();
        user = intent.getStringExtra("appointmentUser");
        getAddedDoctor();
        if (user.equalsIgnoreCase("Patient")) {
            getPatientAppointmentHistory();
        } else {
            requestAppointment.setVisibility(View.GONE);
            getDoctorAppointmentHistory();
        }
        firebaseUtil = new FirebaseUtil(ViewAppointmentsActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ViewAppointmentsAdaptor(ViewAppointmentsActivity.this, appointmentList);
        recyclerView.setAdapter(adapter);


        requestAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAppointmentsActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.request_appointment_dialog, null);
                Button cancel = dialogView.findViewById(R.id.cancelRequestAppointmentDialog);
                final Button addAppointmentRequest = dialogView.findViewById(R.id.addRequestAppointment);
                final Spinner appointmentTypeSpinner = dialogView.findViewById(R.id.appointmentTypeSpinner);
                String[] appointmentTypes = {"Select", "Call", "Video Call", "Visit"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewAppointmentsActivity.this, android.R.layout.simple_list_item_1, appointmentTypes);
                appointmentTypeSpinner.setAdapter(adapter);
                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                addAppointmentRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (appointmentTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            showToast("Please select appointment type");
                            return;
                        }
                        if (addedDoctorID.isEmpty()) {
                            showToast("Please add a doctor first!");
                        } else {
                            AppointmentRequest appointmentRequest = new AppointmentRequest(firebaseUtil.getCurrentUserID(), addedDoctorID, appointmentTypeSpinner.getSelectedItem().toString(),
                                    "Unscheduled");
                            DatabaseReference appointmentRequestRef = FirebaseDatabase.getInstance().getReference().child("AppointmentRequest").
                                    child(firebaseUtil.getCurrentUserID() + "_" + addedDoctorID);
                            appointmentRequestRef.setValue(appointmentRequest);
                            dialog.dismiss();
                            showToast("Appointment Request Sent!");
                        }
                    }
                });
            }
        });
    }

    public void getAddedDoctor() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("AddedDoctors");
        try {
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Request addedDoctor = postSnapshot.getValue(Request.class);
                        if (addedDoctor.patientID.equals(firebaseUtil.getCurrentUserID())) {
                            addedDoctorID = addedDoctor.doctorID;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            Log.d("Reports: ", e.getMessage());
        }
    }

    public void getPatientAppointmentHistory() {
        showHideProgress(true, "");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Appointments");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                        Appointment appointment = snapshot.getValue(Appointment.class);
                        try {
                            if (appointment != null && (appointment.getPatientID().equals(firebaseUtil.getCurrentUserID()))) {
                                appointmentList.add(appointment);
                            }
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                }
                if (appointmentList.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noScheduledAppointments.setVisibility(View.VISIBLE);
                }
                showHideProgress(false, "");
                adapter.setAppointmentList(appointmentList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showHideProgress(false, "");
            }
        });
    }

    public void getDoctorAppointmentHistory() {
        showHideProgress(true, "");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Appointments");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                        Appointment appointment = snapshot.getValue(Appointment.class);
                        try {
                            if (appointment != null && appointment.getDoctorID().equals(firebaseUtil.getCurrentUserID())) {
                                appointmentList.add(appointment);
                            }
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                }
                if (appointmentList.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noScheduledAppointments.setVisibility(View.VISIBLE);
                }
                showHideProgress(false, "");
                adapter.setAppointmentList(appointmentList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showHideProgress(false, "");
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_view_appointments;
    }
}
