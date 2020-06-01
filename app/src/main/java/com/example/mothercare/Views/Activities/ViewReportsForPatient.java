package com.example.mothercare.Views.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.ViewReportsAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Models.Report;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewReportsForPatient extends BaseActivity {
    private RecyclerView recyclerView;
    private TextView noSharedReportsTextView;
    ArrayList<Report> reports = new ArrayList<>();
    private FirebaseUtil firebaseUtil;
    private ViewReportsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.sharedReportsPatientRecylerView);
        noSharedReportsTextView = findViewById(R.id.noReportsSharedPatient);
        firebaseUtil = new FirebaseUtil(this);
        adapter = new ViewReportsAdapter(reports, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        getReports();
    }

    private void getReports() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reports");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        Report report = postSnapShot.getValue(Report.class);
                        if (report.patientID.equals(firebaseUtil.getCurrentUserID())) {
                            reports.add(report);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_view_reports_for_patient;
    }
}
