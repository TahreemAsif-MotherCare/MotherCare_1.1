package com.example.mothercare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.EmergencyContact;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class EmergencyContactsAdapter extends RecyclerView.Adapter<EmergencyContactsAdapter.MyViewHolder> {

    private ArrayList<EmergencyContact> emergencyContacts;
    private FirebaseUtil firebaseUtil;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, contactNumber;
        Button call;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            contactNumber = view.findViewById(R.id.contactNumber);
            call = view.findViewById(R.id.call);
        }
    }


    public EmergencyContactsAdapter(ArrayList<EmergencyContact> emergencyContactArrayList, Context context) {
        this.emergencyContacts = emergencyContactArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emergency_call_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final EmergencyContact emergencyContact = emergencyContacts.get(position);
        holder.name.setText(emergencyContact.name);
        holder.contactNumber.setText(emergencyContact.contactNumber);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + emergencyContact.contactNumber));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emergencyContacts.size();
    }
}