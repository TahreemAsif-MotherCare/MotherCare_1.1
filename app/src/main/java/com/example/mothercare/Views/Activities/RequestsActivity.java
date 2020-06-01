package com.example.mothercare.Views.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.RequestsAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class RequestsActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private RecyclerView requestsRecyclerView;
    private TextView noRequestsFound;
    private RequestsAdapter adapter;
    FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        noRequestsFound = findViewById(R.id.noRequestsFound);
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getRequests();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_requests;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        showHideProgress(false, "");
        switch (firebaseResponses) {
            case PatientRequests: {
                ArrayList<Patient> patientArrayList = (ArrayList<Patient>) o;
                if (!patientArrayList.isEmpty()) {
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    requestsRecyclerView.setLayoutManager(mLayoutManager);
                    requestsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter = new RequestsAdapter(patientArrayList, RequestsActivity.this);
                    requestsRecyclerView.setAdapter(adapter);
                } else {
                    requestsRecyclerView.setVisibility(View.GONE);
                    noRequestsFound.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }
}
