package com.example.mothercare.Views.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.OrderRequestAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.MedicineOrder;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class OrderRequestsActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private FirebaseUtil firebaseUtil;
    OrderRequestAdapter orderRequestAdapter;
    RecyclerView recyclerView;
    TextView noReqTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Order Requests");
        recyclerView = findViewById(R.id.orderRequestsRecyclerView);
        noReqTv = findViewById(R.id.noOrderReqTV);
        firebaseUtil = new FirebaseUtil(this);
        firebaseUtil.setFirebaseResponse(this);
        showHideProgress(true, "Please Wait");
        firebaseUtil.getOrderHistory();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_order_requests;
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case getOrderHistory: {
                ArrayList<MedicineOrder> orderArrayList = (ArrayList<MedicineOrder>) o;
                ArrayList<MedicineOrder> orders = new ArrayList<>();
                for (MedicineOrder medicineOrder : orderArrayList) {
                    if (medicineOrder.getOrderStatus().equals("Pending")) {
                        orders.add(medicineOrder);
                    }
                }
                showHideProgress(false, "");
                if (orders.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    noReqTv.setVisibility(View.VISIBLE);
                } else {
                    noReqTv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    orderRequestAdapter = new OrderRequestAdapter(OrderRequestsActivity.this, orders);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(orderRequestAdapter);
                }
                break;
            }
            case Error:
        }
    }
}
