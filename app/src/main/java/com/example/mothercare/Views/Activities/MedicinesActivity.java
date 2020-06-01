package com.example.mothercare.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.ListViewAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Medicine;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MedicinesActivity extends BaseActivity implements SearchView.OnQueryTextListener, FirebaseUtil.FirebaseResponse {
    LinearLayout searchMedicineLayout;
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    TextView sortByPrice;
    BottomNavigationView bottomNavigationView;
    String[] animalNameList;
    ArrayList<Medicine> arraylist = new ArrayList<Medicine>();
    private RecyclerView medicineRecyclerView;
    FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchMedicineLayout = findViewById(R.id.searchMedicineLayout);
        sortByPrice = findViewById(R.id.sortByPrice);
        int width = getResources().getDisplayMetrics().widthPixels;
        int hei = getResources().getDisplayMetrics().heightPixels / 4;
        searchMedicineLayout.setLayoutParams(new ConstraintLayout.LayoutParams(width, hei));
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getAllMedicines();

        medicineRecyclerView = findViewById(R.id.listview);

        getSupportActionBar().setTitle("Buy Required Medicines From Here");
        // Binds the Adapter to the ListView
        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        medicineRecyclerView.setLayoutManager(mLayoutManager);
        medicineRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListViewAdapter(this, arraylist, "Patient");
        medicineRecyclerView.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.searchMedicine);
        editsearch.setOnQueryTextListener(this);

        sortByPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(arraylist, new Comparator<Medicine>() {
                    @Override
                    public int compare(Medicine p1, Medicine p2) {
                        return p1.price - p2.price; // Descending
                    }
                });
                adapter = new ListViewAdapter(MedicinesActivity.this, arraylist, "Patient");
                adapter.notifyDataSetChanged();
                medicineRecyclerView.setAdapter(adapter);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.cart) {
                    Intent intent = new Intent(MedicinesActivity.this, CartActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_medicines;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case getMedicines: {
                ArrayList<Medicine> medicineArrayList = (ArrayList<Medicine>) o;
                GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                medicineRecyclerView.setLayoutManager(mLayoutManager);
                medicineRecyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new ListViewAdapter(this, medicineArrayList, "Patient");
                medicineRecyclerView.setAdapter(adapter);
                showHideProgress(false, "");
                break;
            }
            case Error: {
                Log.d("TAG", "firebaseResponse: ");
                break;
            }
        }
    }
}
