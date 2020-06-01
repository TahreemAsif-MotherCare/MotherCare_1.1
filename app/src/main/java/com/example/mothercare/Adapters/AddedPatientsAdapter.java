package com.example.mothercare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Views.Activities.AddReportsActivity;
import com.example.mothercare.Views.Activities.DoctorChatActivity;
import com.example.mothercare.Views.Activities.PatientProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AddedPatientsAdapter extends RecyclerView.Adapter<AddedPatientsAdapter.MyViewHolder> {

    private ArrayList<Patient> patientArrayList;
    private FirebaseUtil firebaseUtil;
    private Context context;
    private String currentFlow;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView patientName;
        RatingBar ratingBar;
        ImageView doctorProfilePicture, call, message;

        public MyViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.addedDoctorName);
            ratingBar = view.findViewById(R.id.ratingBar);
            doctorProfilePicture = view.findViewById(R.id.doctorProfilePicture);
            call = view.findViewById(R.id.callPatient);
            message = view.findViewById(R.id.messagePatient);
        }
    }


    public AddedPatientsAdapter(ArrayList<Patient> patients, Context context, String currentFlow) {
        this.patientArrayList = patients;
        this.context = context;
        this.currentFlow = currentFlow;
        firebaseUtil = new FirebaseUtil(context);
        notifyDataSetChanged();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.added_doctor_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Patient patient = patientArrayList.get(position);
        holder.patientName.setText(patient.username);
        holder.ratingBar.setVisibility(View.GONE);
//        holder.doctorProfilePicture.set

        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("Patient").child(patient.patientID);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.doctorProfilePicture.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFlow.equalsIgnoreCase("AddReportActivity")) {
                    Intent intent = new Intent(context, AddReportsActivity.class);
                    intent.putExtra("patientID", patient.patientID);
                    context.startActivity(intent);
                } else if (currentFlow.equalsIgnoreCase("Registered Patients")) {
                    Intent intent = new Intent(context, PatientProfile.class);
                    intent.putExtra("PatientID", patient.patientID);
                    context.startActivity(intent);
                }
            }
        });
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorChatActivity.class);
                intent.putExtra("PatientID", patient.patientID);
                context.startActivity(intent);
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + patient.phoneNumber));
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientArrayList.size();
    }
}