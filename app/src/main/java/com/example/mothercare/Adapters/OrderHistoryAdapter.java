package com.example.mothercare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Medicine;
import com.example.mothercare.Models.MedicineOrder;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private ArrayList<MedicineOrder> orderArrayList;
    private FirebaseUtil firebaseUtil;
    private Context context;
    String pharmacyName, medicines;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, orderedBy, pharmacyName, orderDate, address, phoneNumber, medicines, orderPrice;

        public MyViewHolder(View view) {
            super(view);
            orderID = (TextView) view.findViewById(R.id.orderID);
            orderedBy = (TextView) view.findViewById(R.id.orderedBy);
            pharmacyName = (TextView) view.findViewById(R.id.pharmacyName);
            orderDate = (TextView) view.findViewById(R.id.orderDate);
            medicines = (TextView) view.findViewById(R.id.medicines);
            orderPrice = (TextView) view.findViewById(R.id.orderPrice);
            address = (TextView) view.findViewById(R.id.orderAdress);
            phoneNumber = (TextView) view.findViewById(R.id.phoneNumber);
        }
    }


    public OrderHistoryAdapter(Context context, ArrayList<MedicineOrder> orderArrayList) {
        this.orderArrayList = orderArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MedicineOrder order = orderArrayList.get(position);
        holder.orderID.setText(order.orderID);
        holder.orderedBy.setText(order.patient.username);
        for (Medicine medicine : order.medicine) {
            pharmacyName = medicine.pharmacyName + ", ";
            medicines = medicine.medicineName + ", ";
        }
        holder.pharmacyName.setText(pharmacyName);
        holder.medicines.setText(medicines);
        holder.orderPrice.setText(String.valueOf(order.orderPrice));
        holder.orderDate.setText(order.orderDate);
        holder.address.setText(order.patient.email);
        holder.phoneNumber.setText(order.patient.phoneNumber);
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
}