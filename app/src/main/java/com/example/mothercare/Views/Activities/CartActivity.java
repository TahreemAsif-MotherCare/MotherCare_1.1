package com.example.mothercare.Views.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    FirebaseUtil firebaseUtil;
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
                    final String orderID = generateRandomID();
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CartActivity.this);
                    dialogBuilder.setTitle("Place Order");
                    dialogBuilder.setMessage("Are you sure you want to place this order? ");

                    dialogBuilder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            MedicineOrder medicineOrder = new MedicineOrder(orderID, cartArrayList, dtf.format(now), getPrice(),
                                    patient);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order").child(orderID);
                            reference.setValue(medicineOrder);
                            showToast("Order Placed Successfully");
                            cartArrayList.clear();
                            finish();
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
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case SpecificPatientInformation: {
                patient = (Patient) o;
                showHideProgress(false, "");
            }
            case Error: {
            }
        }
    }
}
