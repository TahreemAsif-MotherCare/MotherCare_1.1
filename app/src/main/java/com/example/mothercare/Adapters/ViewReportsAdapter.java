package com.example.mothercare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Report;
import com.example.mothercare.R;
import com.example.mothercare.Views.Activities.PrescriptionPicActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ViewReportsAdapter extends RecyclerView.Adapter<ViewReportsAdapter.MyViewHolder> {
    ArrayList<Report> reportArrayList = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reports_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public ViewReportsAdapter(ArrayList<Report> reports, Context context) {
        this.reportArrayList = reports;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Report report = reportArrayList.get(position);
        holder.descrpiton.setText(report.description);
        if (report.date == null || report.date.isEmpty()) {
            holder.reportHeading.setText("Could not get date!");
        } else {
            holder.reportHeading.setText(report.date);
        }

        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("Reports").child(report.reportID);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.reportPic.setImageBitmap(bitmap);
                report.setReportPicture(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        holder.reportPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1 = new Intent(context, PrescriptionPicActivity.class);
                in1.putExtra("ID", report.reportID);
                in1.putExtra("type", "Image");
                context.startActivity(in1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void resetArray() {
        this.reportArrayList = new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reportHeading;
        public TextView descrpiton;
        public ImageView reportPic;

        public MyViewHolder(View view) {
            super(view);
            reportPic = view.findViewById(R.id.reportPicture);
            reportHeading = view.findViewById(R.id.reportHeading);
            descrpiton = view.findViewById(R.id.reportDescriptionTextView);
        }
    }
}
