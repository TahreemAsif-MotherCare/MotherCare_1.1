package com.example.mothercare.Views.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.CartAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Medicine;
import com.example.mothercare.Models.MedicineOrder;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CartActivity extends BaseActivity implements CartAdapter.CartUpdate, FirebaseUtil.FirebaseResponse {
    CartAdapter cartAdapter;
    RecyclerView cartRecyclerView;
    public static ArrayList<Medicine> cartArrayList = new ArrayList<>();
    int price = 0;
    TextView total;
    Button placeOrder;
    ImageView prescription;
    FirebaseUtil firebaseUtil;
    private static final int GET_FROM_GALLERY = 101;
    Bitmap bitmap = null;
    Patient patient = new Patient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cartRecyclerView = findViewById(R.id.recycler_cart);
        placeOrder = findViewById(R.id.btn_placeorder);
        total = findViewById(R.id.tv_total);
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        showHideProgress(true, "Please Wait");
        firebaseUtil.getPatientInfo(firebaseUtil.getCurrentUserID());
        cartAdapter = new CartAdapter(CartActivity.this, cartArrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        cartRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cartRecyclerView.setLayoutManager(mLayoutManager);
        cartRecyclerView.setAdapter(cartAdapter);
        updatePrice();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartArrayList.size() == 0) {
                    showToast("Please add items in the cart first!");
                } else {
                    if (bitmap == null) {
                        uploadPrescription();
                    } else {
                        placeOrder();
                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_cart;
    }

    @Override
    public void cartUpdate(ArrayList<Medicine> cartArrayList) {
        this.cartArrayList = cartArrayList;
        updatePrice();
    }

    private void uploadPrescription() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CartActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.upload_prescription_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Upload Prescription");
        dialogBuilder.setMessage("Please upload your prescription so that pharmacist can confirm your order. ");

        prescription = dialogView.findViewById(R.id.prescriptionPic);
        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        dialogBuilder.setPositiveButton("Upload Prescription", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (bitmap == null) {
                    showToast("Please upload prescription!");
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

    private void placeOrder() {
        final String orderID = generateRandomID();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CartActivity.this);
        dialogBuilder.setTitle("Place Order");
        dialogBuilder.setMessage("Are you sure you want to place this order? ");

        dialogBuilder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showHideProgress(true, "");
                firebaseUtil.uploadPrescriptionPicture(bitmap, orderID);
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

    private void updatePrice() {
        if (cartArrayList.size() == 0) {
            total.setText("0");
        } else {
            for (Medicine medicine : cartArrayList) {
                price += medicine.price * medicine.cartQuantity;
                total.setText(String.valueOf(price));
            }
        }
    }

    private int getPrice() {
        for (Medicine medicine : cartArrayList) {
            price += medicine.price * medicine.cartQuantity;
        }
        return price;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                prescription.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case SpecificPatientInformation: {
                patient = (Patient) o;
                showHideProgress(false, "");
                break;
            }
            case uploadPrescription: {
                String orderID = (String) o;
                if (!orderID.isEmpty()) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    MedicineOrder medicineOrder = new MedicineOrder(orderID, cartArrayList, dtf.format(now), getPrice(),
                            patient);
                    medicineOrder.setOrderStatus("Pending");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order").child(orderID);
                    reference.setValue(medicineOrder);
                    showToast("Order Placed Successfully");
                    cartArrayList.clear();
                    showHideProgress(false, "");
                    finish();
                }
                break;
            }
            case Error: {
            }
        }
    }
}
