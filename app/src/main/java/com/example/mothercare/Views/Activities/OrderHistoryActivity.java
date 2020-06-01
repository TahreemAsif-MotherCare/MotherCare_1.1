package com.example.mothercare.Views.Activities;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.OrderHistoryAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.MedicineOrder;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class OrderHistoryActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    RecyclerView orderHistoryRecyclerView;
    FirebaseUtil firebaseUtil;
    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Order History");
        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);
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
                orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                orderHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
                orderHistoryRecyclerView.setLayoutManager(mLayoutManager);
                orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
                showHideProgress(false, "");
                break;
            }
            case Error: {
                showToast("Unexpected Error Occured!");
                break;
            }
        }
    }
}
