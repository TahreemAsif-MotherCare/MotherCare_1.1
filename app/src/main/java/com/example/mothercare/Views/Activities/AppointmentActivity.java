package com.example.mothercare.Views.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.ViewAppointmentsAdaptor;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Appointment;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class AppointmentActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private RecyclerView recyclerView;
    private TextView noAppointment;
    private ViewAppointmentsAdaptor mAdapter;
    private FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Appointments");
        init();
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getAppointments("");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_appointments;
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.appointments_recyclerView);
        noAppointment = (TextView) findViewById(R.id.noAppointment);
        firebaseUtil = new FirebaseUtil(AppointmentActivity.this);
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case Appointments: {
                ArrayList<Appointment> appointments = new ArrayList<>();
                if (!appointments.isEmpty()) {
                    mAdapter = new ViewAppointmentsAdaptor(AppointmentActivity.this, appointments);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(mAdapter);
                } else {
                    noAppointment.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }
    }
}
