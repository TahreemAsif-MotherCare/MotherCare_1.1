package com.example.mothercare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Views.Activities.RequestsActivity;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.MyViewHolder> {

    private ArrayList<Patient> requestArrayList;
    private FirebaseUtil firebaseUtil;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView patientName;
        ImageView patientProfilePicture, acceptRequest, rejectRequest;

        public MyViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.patientName);
            patientProfilePicture = view.findViewById(R.id.patientProfilePicture);
            acceptRequest = view.findViewById(R.id.acceptRequest);
            rejectRequest = view.findViewById(R.id.rejectRequest);
        }
    }


    public RequestsAdapter(ArrayList<Patient> patientRequestsArray, Context context) {
        this.requestArrayList = patientRequestsArray;
        this.context = context;
        firebaseUtil = new FirebaseUtil(context);
        notifyDataSetChanged();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.requests_item_ro, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Patient request = requestArrayList.get(position);
        holder.patientName.setText(request.username);
        holder.acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUtil.addFriend(context, request.patientID);
                requestArrayList.remove(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }
}