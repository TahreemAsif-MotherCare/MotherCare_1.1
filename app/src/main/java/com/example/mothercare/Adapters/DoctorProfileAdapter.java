package com.example.mothercare.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.DoctorProfile;
import com.example.mothercare.R;

import java.util.ArrayList;

public class DoctorProfileAdapter extends RecyclerView.Adapter<DoctorProfileAdapter.MyViewHolder> {
    private ArrayList<DoctorProfile> profile;

    public DoctorProfileAdapter(ArrayList<DoctorProfile> profile) {
        this.profile = profile;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_profile_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DoctorProfile doctorProfile = profile.get(position);
        holder.key.setText(doctorProfile.key);
        if (doctorProfile.value.contains("Start") || doctorProfile.value.contains("End"))
            holder.value.setText("N/A");
        else
            holder.value.setText(doctorProfile.value);
    }

    @Override
    public int getItemCount() {
        return profile.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView key, value;

        public MyViewHolder(View view) {
            super(view);
            key = view.findViewById(R.id.profileDetailKey);
            value = view.findViewById(R.id.profileDetail);
        }
    }
}
