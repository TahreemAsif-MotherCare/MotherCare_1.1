package com.example.mothercare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Medicine;
import com.example.mothercare.Models.MedicineOrder;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Views.Activities.PrescriptionPicActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.MyViewHolder> {

    private ArrayList<MedicineOrder> orderArrayList;
    private FirebaseUtil firebaseUtil;
    private Context context;
    String pharmacyName, medicines;
    Bitmap bitmap;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderedBy, orderDate, address, phoneNumber, medicines, orderPrice;
        Button acceptOrder, rejectOrder;
        ImageView prescriptionPic;

        public MyViewHolder(View view) {
            super(view);
            orderedBy = (TextView) view.findViewById(R.id.orderedBy);
            orderDate = (TextView) view.findViewById(R.id.orderDate);
            medicines = (TextView) view.findViewById(R.id.medicines);
            orderPrice = (TextView) view.findViewById(R.id.orderPrice);
            address = (TextView) view.findViewById(R.id.orderAdress);
            phoneNumber = (TextView) view.findViewById(R.id.phoneNumber);
            acceptOrder = view.findViewById(R.id.acceptOrderReq);
            rejectOrder = view.findViewById(R.id.rejectOrderReq);
            prescriptionPic = view.findViewById(R.id.prescriptionPic);
        }
    }


    public OrderRequestAdapter(Context context, ArrayList<MedicineOrder> orderArrayList) {
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
                return date1.compareTo(date2); // Descending
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_request_item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MedicineOrder order = orderArrayList.get(position);
        holder.orderedBy.setText(order.patient.username);
        for (Medicine medicine : order.medicine) {
            pharmacyName = medicine.pharmacyName + ", ";
            medicines = medicine.medicineName + " (" + medicine.cartQuantity + "), ";
        }
        holder.medicines.setText(medicines);
        holder.orderPrice.setText(String.valueOf(order.orderPrice));
        holder.orderDate.setText(order.orderDate);
        holder.address.setText(order.patient.email);
        holder.phoneNumber.setText(order.patient.phoneNumber);

        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("Prescriptions").child(order.orderID);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.prescriptionPic.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        holder.prescriptionPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent in1 = new Intent(context, PrescriptionPicActivity.class);
                in1.putExtra("image", byteArray);
                in1.putExtra("type", "Order");
                context.startActivity(in1);
            }
        });
        holder.acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Order").child(order.orderID)
                        .child("orderStatus").setValue("Confirmed");
                orderArrayList.remove(order);
                notifyDataSetChanged();
            }
        });
        holder.rejectOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order").child(order.orderID);
                reference.removeValue();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
}