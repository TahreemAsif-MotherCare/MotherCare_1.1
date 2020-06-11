package com.example.mothercare.Views.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.ListViewAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Medicine;
import com.example.mothercare.Models.Pharmacist;
import com.example.mothercare.Models.UserLocation;
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
    TextView sortByPrice, noMedFound, showResults;
    BottomNavigationView bottomNavigationView;
    ArrayList<Medicine> arraylist = new ArrayList<Medicine>();
    private RecyclerView medicineRecyclerView;
    FirebaseUtil firebaseUtil;
    int radius = 5;
    int checkedItem = 0;
    ArrayList<Pharmacist> pharmacies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchMedicineLayout = findViewById(R.id.searchMedicineLayout);
        sortByPrice = findViewById(R.id.sortByPrice);
        showResults = findViewById(R.id.showResultsTV);
        getSupportActionBar().setTitle("Medicines");
        noMedFound = findViewById(R.id.noMedFound);
        int width = getResources().getDisplayMetrics().widthPixels;
        int hei = getResources().getDisplayMetrics().heightPixels / 4;
//        searchMedicineLayout.setLayoutParams(new ConstraintLayout.LayoutParams(width, hei));
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getPharmacies();

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

        showResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MedicinesActivity.this);
                alertDialog.setTitle("Show Results from");
                String[] items = {"5km", "10km", "Show All Results"};

                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                radius = 5;
                                firebaseUtil.getPharmacies();
                                checkedItem = 0;
                                dialog.dismiss();
                                break;
                            case 1:
                                radius = 10;
                                firebaseUtil.getPharmacies();
                                checkedItem = 1;
                                dialog.dismiss();
                                break;
                            case 2:
                                radius = 2000;
                                firebaseUtil.getPharmacies();
                                checkedItem = 2;
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
            case Pharmacist: {
                ArrayList<Pharmacist> pharmacistArrayList = (ArrayList<Pharmacist>) o;

                UserLocation userLocation = getLocation();
                for (Pharmacist pharmacist : pharmacistArrayList) {
                    if (distance(userLocation.latitude, userLocation.longitude, pharmacist.getLocation().latitude, pharmacist.getLocation().longitude) < radius) {
                        pharmacies.add(pharmacist);
                    }
                }
                firebaseUtil.getAllMedicines();
                break;
            }
            case getMedicines: {
                ArrayList<Medicine> medicineArrayList = (ArrayList<Medicine>) o;
                ArrayList<Medicine> medicines = new ArrayList<>();
                if (radius != 2000) {
                    for (Medicine medicine : medicineArrayList) {
                        for (Pharmacist pharmacist : pharmacies) {
                            if (!medicine.pharmacyID.equals(pharmacist.pharmacistID)) {
                                medicines.add(medicine);
                            }
                        }
                    }
                } else
                    medicines = medicineArrayList;
                if (medicines.isEmpty()) {
                    noMedFound.setVisibility(View.VISIBLE);
                    medicineRecyclerView.setVisibility(View.GONE);
                } else {
                    if (noMedFound.getVisibility() == View.VISIBLE) {
                        noMedFound.setVisibility(View.GONE);
                        medicineRecyclerView.setVisibility(View.VISIBLE);
                    }
                    GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    medicineRecyclerView.setLayoutManager(mLayoutManager);
                    medicineRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter = new ListViewAdapter(this, medicines, "Patient");
                    medicineRecyclerView.setAdapter(adapter);
                }
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
