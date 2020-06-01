package com.example.mothercare.Views.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FindDoctor extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    FloatingActionButton findDoctorByCardView;
    private RecyclerView recyclerView;
    private TextView sortByRating;
    private AddedDoctorsAdapter addedDoctorsAdapter;
    private FirebaseUtil firebaseUtil;
    ArrayList<Doctor> addedDoctorDetailArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);
        getSupportActionBar().setTitle("Find Doctor");
        findDoctorByCardView = findViewById(R.id.findDoctorByLocation);
        recyclerView = findViewById(R.id.allDoctorsRecyclerView);
        sortByRating = findViewById(R.id.sortByRating);
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getDoctors();

        findDoctorByCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindDoctor.this, LocationActivity.class);
                startActivity(intent);
            }
        });
        sortByRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FindDoctor.this);
                alertDialog.setTitle("Sort");
                String[] items = {"Sort by Rating", "Sort by Charges", "Sort by Experience"};
                int checkedItem = 1;
                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Collections.sort(addedDoctorDetailArrayList, new Comparator<Doctor>() {
                                    @Override
                                    public int compare(Doctor p1, Doctor p2) {
                                        return p2.rating - p1.rating; // Ascending
                                    }
                                });
                                addedDoctorsAdapter = new AddedDoctorsAdapter(addedDoctorDetailArrayList, FindDoctor.this);
                                addedDoctorsAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(addedDoctorsAdapter);
                                dialog.dismiss();
                                break;
                            case 1:
                                Collections.sort(addedDoctorDetailArrayList, new Comparator<Doctor>() {
                                    @Override
                                    public int compare(Doctor p1, Doctor p2) {
                                        return p1.charges - p2.charges; // Descending
                                    }
                                });
                                addedDoctorsAdapter = new AddedDoctorsAdapter(addedDoctorDetailArrayList, FindDoctor.this);
                                addedDoctorsAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(addedDoctorsAdapter);
                                dialog.dismiss();
                                break;
                            case 2:
                                Collections.sort(addedDoctorDetailArrayList, new Comparator<Doctor>() {
                                    @Override
                                    public int compare(Doctor p1, Doctor p2) {
                                        return p2.experience - p1.experience; // Ascending
                                    }
                                });
                                addedDoctorsAdapter = new AddedDoctorsAdapter(addedDoctorDetailArrayList, FindDoctor.this);
                                addedDoctorsAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(addedDoctorsAdapter);
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_find_doctor;
    }


    @Override
    public void firebaseResponse(Object response, FirebaseResponses firebaseResponses) {
        showHideProgress(false, "");
        switch (firebaseResponses) {
            case Doctors: {
                addedDoctorDetailArrayList = (ArrayList<Doctor>) response;
                if (!addedDoctorDetailArrayList.isEmpty()) {
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    addedDoctorsAdapter = new AddedDoctorsAdapter(addedDoctorDetailArrayList, this);
                    recyclerView.setAdapter(addedDoctorsAdapter);
                }
            }
            case Error:
        }
    }
}
