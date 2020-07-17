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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private ArrayList<MedicineOrder> orderArrayList;
    private FirebaseUtil firebaseUtil;
    private Context context;
    String pharmacyName, medicines;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, orderedBy, pharmacyName, orderDate, address, phoneNumber, medicines, orderPrice, orderStatus;

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
            orderStatus = (TextView) view.findViewById(R.id.orderStatus);
        }
    }


    public OrderHistoryAdapter(Context context, ArrayList<MedicineOrder> orderArrayList) {
        this.orderArrayList = orderArrayList;
        this.context = context;

        Collections.sort(this.orderArrayList, new Comparator<MedicineOrder>() {
            @Override
            public int compare(MedicineOrder p1, MedicineOrder p2) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date1 = new Date();
                Date date2 = new Date();
                try {
                    date1 = sdf.parse(p1.orderDate);
                    date2 = sdf.parse(p2.orderDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date2.compareTo(date1);
            }
        });
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
        holder.orderStatus.setText(order.getOrderStatus());
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
}