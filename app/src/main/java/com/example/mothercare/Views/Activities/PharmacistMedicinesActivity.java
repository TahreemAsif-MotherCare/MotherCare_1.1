package com.example.mothercare.Views.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.ListViewAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Medicine;
import com.example.mothercare.Models.Pharmacist;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class PharmacistMedicinesActivity extends BaseActivity implements SearchView.OnQueryTextListener, FirebaseUtil.FirebaseResponse {
    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] animalNameList;
    ArrayList<Medicine> arraylist = new ArrayList<Medicine>();
    private RecyclerView medicineRecyclerView;
    private FloatingActionButton addMedicine;
    private static final int GET_FROM_GALLERY = 101;
    Uri selectedImage;
    ImageView medicinePicture;
    Bitmap bitmap;
    FirebaseUtil firebaseUtil;
    Pharmacist pharmacist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addMedicine = findViewById(R.id.addMedicine);
        medicineRecyclerView = findViewById(R.id.listview);
        showHideProgress(true, "Please Wait");
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getPharmacistInfo();

        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PharmacistMedicinesActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_medicine_dialog, null);
                dialogBuilder.setView(dialogView);
                final EditText medicineName = dialogView.findViewById(R.id.medicineName);
                final EditText medicineDose = dialogView.findViewById(R.id.medicinePower);
                final EditText medicineQuantity = dialogView.findViewById(R.id.medicineQuantity);
                final EditText medicinePrice = dialogView.findViewById(R.id.medicinePrice);
                medicinePicture = dialogView.findViewById(R.id.medicinePicture);

                medicinePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                    }
                });

                dialogBuilder.setPositiveButton("Add Medicine", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (medicineName.getText().toString().isEmpty()) {
                            medicineName.setError("Medicine name cannot be empty!");
                            medicineName.requestFocus();
                        } else if (medicineDose.getText().toString().isEmpty()) {
                            medicineDose.setError("Medicine dose cannot be empty!");
                            medicineDose.requestFocus();
                        } else if (medicineQuantity.getText().toString().isEmpty()) {
                            medicineQuantity.setError("Medicine quantity cannot be empty!");
                            medicineQuantity.requestFocus();
                        } else if (medicinePrice.getText().toString().isEmpty()) {
                            medicinePrice.setError("Medicine price cannot be empty!");
                            medicinePrice.requestFocus();
                        } else {
                            Medicine medicine = new Medicine(generateRandomID(), medicineName.getText().toString(), pharmacist.username, medicineDose.getText().toString(),
                                    Integer.valueOf(medicinePrice.getText().toString()), Integer.valueOf(medicineQuantity.getText().toString()), firebaseUtil.getCurrentUserID());
                            showHideProgress(true, "Please Wait");
                            firebaseUtil.uploadMedicine(medicine, bitmap);
                        }
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        // Pass results to ListViewAdapter Class

        // Binds the Adapter to the ListView


        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.searchMedicine);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pharmacist_medicines;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                medicinePicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case Pharmacist: {
                pharmacist = (Pharmacist) o;
                firebaseUtil.getSpecificPharmacyMedicines();
                break;
            }
            case getMedicines: {
                ArrayList<Medicine> medicineArrayList = (ArrayList<Medicine>) o;
                GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                medicineRecyclerView.setLayoutManager(mLayoutManager);
                medicineRecyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new ListViewAdapter(this, medicineArrayList, "Pharmacy");
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
