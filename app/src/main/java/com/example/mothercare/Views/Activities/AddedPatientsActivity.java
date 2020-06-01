package com.example.mothercare.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.AddedPatientsAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class AddedPatientsActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private RecyclerView recyclerView;
    private AddedPatientsAdapter addedPatientAdapter;
    private TextView noPatientAdded, addedDoctorsTextView;
    private FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.addedDoctorsRecylerView);
        addedDoctorsTextView = findViewById(R.id.addedDoctorsTextView);
        noPatientAdded = findViewById(R.id.noDoctorsAdded);
        addedDoctorsTextView.setText("Added Patients");
        firebaseUtil = new FirebaseUtil(AddedPatientsActivity.this);
        firebaseUtil.setFirebaseResponse(this);
        showHideProgress(true, "");
        firebaseUtil.getAddedPatients();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_added_doctors;
    }


    @Override
    public void firebaseResponse(Object response, FirebaseResponses firebaseResponses) {
        showHideProgress(false, "");
        switch (firebaseResponses) {
            case AddedPatients: {
                ArrayList<Patient> patientArrayList = (ArrayList<Patient>) response;
                if (patientArrayList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    noPatientAdded.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = getIntent();
                    String currentFlow = intent.getStringExtra("currentFlow");
                    GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    addedPatientAdapter = new AddedPatientsAdapter(patientArrayList, AddedPatientsActivity.this, currentFlow);
                    recyclerView.setAdapter(addedPatientAdapter);
                }
                break;
            }
            case Error: {

            }
        }
    }
}
