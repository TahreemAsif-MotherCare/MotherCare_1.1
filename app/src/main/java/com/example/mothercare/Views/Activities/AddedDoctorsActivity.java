package com.example.mothercare.Views.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.AddedDoctorsAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Doctor;
import com.example.mothercare.Models.Request;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class AddedDoctorsActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private RecyclerView recyclerView;
    private AddedDoctorsAdapter addedDoctorsAdapter;
    private TextView noDoctorsAdded;
    private FirebaseUtil firebaseUtil;
    Request addedDoctor;
    ArrayList<Request> addedDoctorArrayList = new ArrayList<>();
    ArrayList<Doctor> addedDoctorDetailArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.addedDoctorsRecylerView);
        noDoctorsAdded = findViewById(R.id.noDoctorsAdded);
        firebaseUtil = new FirebaseUtil(AddedDoctorsActivity.this);
        firebaseUtil.setFirebaseResponse(this);
        showHideProgress(true, "");
        firebaseUtil.getAddedDoctors();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_added_doctors;
    }

    @Override
    public void firebaseResponse(Object response, FirebaseResponses firebaseResponses) {
        showHideProgress(false, "");
        switch (firebaseResponses) {
            case AddedDoctors: {
                ArrayList<Doctor> doctorArrayList = (ArrayList<Doctor>) response;
                if (!doctorArrayList.isEmpty()) {
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    addedDoctorsAdapter = new AddedDoctorsAdapter(addedDoctorDetailArrayList, AddedDoctorsActivity.this);
                    recyclerView.setAdapter(addedDoctorsAdapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noDoctorsAdded.setVisibility(View.VISIBLE);
                }
            }
            case Error: {

            }
        }
    }
}
