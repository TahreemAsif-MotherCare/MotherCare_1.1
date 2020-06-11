package com.example.mothercare.Views.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.OrderHistoryAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Medicine;
import com.example.mothercare.Models.MedicineOrder;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class OrderHistoryActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    RecyclerView orderHistoryRecyclerView;
    FirebaseUtil firebaseUtil;
    OrderHistoryAdapter orderHistoryAdapter;
    TextView noOrderHistoryTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Order History");
        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);
        noOrderHistoryTv = findViewById(R.id.noOrderHisTV);
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        showHideProgress(true, "Please Wait");
        firebaseUtil.getOrderHistory();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_order_history;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case getOrderHistory: {
                ArrayList<MedicineOrder> orderArrayList = (ArrayList<MedicineOrder>) o;
                ArrayList<MedicineOrder> orders = new ArrayList<>();
                for (MedicineOrder medicineOrder : orderArrayList) {
                    if (medicineOrder.getOrderStatus().equals("Confirmed")) {
                        if (medicineOrder.patient.patientID.equals(firebaseUtil.getCurrentUserID())) {
                            orders.add(medicineOrder);
                        } else {
                            for (Medicine medicine : medicineOrder.medicine) {
                                if (medicine.pharmacyID.equals(firebaseUtil.getCurrentUserID())) {
                                    orders.add(medicineOrder);
                                    break;
                                }
                            }
                        }
                    }
                }
                showHideProgress(false, "");
                if (orders.isEmpty()) {
                    orderHistoryRecyclerView.setVisibility(View.GONE);
                    noOrderHistoryTv.setVisibility(View.VISIBLE);
                } else {
                    noOrderHistoryTv.setVisibility(View.GONE);
                    orderHistoryRecyclerView.setVisibility(View.VISIBLE);
                    orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    orderHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    orderHistoryRecyclerView.setLayoutManager(mLayoutManager);
                    orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
                }
                break;
            }
            case Error: {
                showToast("Unexpected Error Occured!");
                break;
            }
        }
    }
}
