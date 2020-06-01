package com.example.mothercare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Doctor;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Views.Activities.DoctorProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddedDoctorsAdapter extends RecyclerView.Adapter<AddedDoctorsAdapter.MyViewHolder> {

    private ArrayList<Doctor> doctorArrayList;
    private FirebaseUtil firebaseUtil;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, experience, charges;
        RatingBar ratingBar;
        CircleImageView doctorProfilePicture;
        LinearLayout contactLayout;

        public MyViewHolder(View view) {
            super(view);
            doctorName = (TextView) view.findViewById(R.id.addedDoctorName);
            experience = (TextView) view.findViewById(R.id.experience);
            charges = (TextView) view.findViewById(R.id.charges);
            ratingBar = view.findViewById(R.id.ratingBar);
            doctorProfilePicture = view.findViewById(R.id.doctorProfilePicture);
            contactLayout = view.findViewById(R.id.contactLayout);
        }
    }


    public AddedDoctorsAdapter(ArrayList<Doctor> doctors, Context context) {
        this.doctorArrayList = doctors;
        this.context = context;
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
        final Doctor doctor = doctorArrayList.get(position);
        holder.contactLayout.setVisibility(View.GONE);
        holder.doctorName.setText(doctor.username);
        holder.ratingBar.setEnabled(false);
        holder.ratingBar.setRating(doctor.rating);
        holder.experience.setText("Experience: " + doctor.experience + " years");
        holder.charges.setText("Fee: " + doctor.charges + "/Month");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorProfileActivity.class);
                intent.putExtra("doctorID", doctor.doctorID);
                context.startActivity(intent);
            }
        });

        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("Doctor").child(doctor.doctorID);
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
    }

    @Override
    public int getItemCount() {
        return doctorArrayList.size();
    }
}