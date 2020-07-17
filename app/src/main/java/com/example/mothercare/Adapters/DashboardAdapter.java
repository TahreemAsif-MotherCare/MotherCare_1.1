package com.example.mothercare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Dashboard;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Views.Activities.AddReportsActivity;
import com.example.mothercare.Views.Activities.AddedDoctorsActivity;
import com.example.mothercare.Views.Activities.AddedPatientsActivity;
import com.example.mothercare.Views.Activities.AppointmentActivity;
import com.example.mothercare.Views.Activities.DashboardActivity;
import com.example.mothercare.Views.Activities.DiscussionActivity;
import com.example.mothercare.Views.Activities.FindDoctor;
import com.example.mothercare.Views.Activities.MedicineRemindersActivity;
import com.example.mothercare.Views.Activities.OrderHistoryActivity;
import com.example.mothercare.Views.Activities.OrderRequestsActivity;
import com.example.mothercare.Views.Activities.PharmacistMedicinesActivity;
import com.example.mothercare.Views.Activities.RequestsActivity;
import com.example.mothercare.Views.Activities.SymptomsActivity;
import com.example.mothercare.Views.Activities.ViewAppointmentRequests;
import com.example.mothercare.Views.Activities.ViewAppointmentsActivity;
import com.example.mothercare.Views.Activities.YourDoctor;
import com.example.mothercare.Views.Activities.webCrawler.InformationActivity;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> implements FirebaseUtil.FirebaseResponse {

    private List<Dashboard> dashboardList;
    private Context context;
    private FirebaseUtil firebaseUtil;

    @Override
    public void firebaseResponse(Object response, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case PatientsAddedDoctor: {
                if (!response.equals("")) {
                    Intent intent = new Intent(context, YourDoctor.class);
                    intent.putExtra("DoctorID", String.valueOf(response));
                    context.startActivity(intent);
                } else {
                    ((DashboardActivity) context).showToast("You have added no doctor. Please add one first!");
                }
                break;
            }
            case Error: {
                ((DashboardActivity) context).showToast("Error: " + String.valueOf(response));
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView itemDrawable;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.dashboardItemTitle);
            itemDrawable = view.findViewById(R.id.dashboardImageView);
        }
    }


    public DashboardAdapter(List<Dashboard> dashboardList, Context context) {
        this.dashboardList = dashboardList;
        this.context = context;
        firebaseUtil = new FirebaseUtil(context);
        firebaseUtil.setFirebaseResponse(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Dashboard item = dashboardList.get(position);
        holder.title.setText(item.itemName);
        holder.itemDrawable.setImageResource(item.itemDrawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.title.getText().toString().equalsIgnoreCase("Requests")) {
                    Intent intent = new Intent(context, RequestsActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Your Doctor")) {
                    firebaseUtil.getAddedDoctor();
                } else if (holder.title.getText().toString().contains("Make Appointments")) {
                    Intent intent = new Intent(context, AppointmentActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().contains("View Appointments")) {
                    Intent intent = new Intent(context, ViewAppointmentsActivity.class);
                    intent.putExtra("appointmentUser", "Patient");
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().contains("Order History")) {
                    Intent intent = new Intent(context, OrderHistoryActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().contains("Scheduled Appointments")) {
                    Intent intent = new Intent(context, ViewAppointmentsActivity.class);
                    intent.putExtra("appointmentUser", "Doctor");
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().contains("Medicine Reminders")) {
                    Intent intent = new Intent(context, MedicineRemindersActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().contains("Added Doctors")) {
                    Intent intent = new Intent(context, AddedDoctorsActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Appointment Requests")) {
                    Intent intent = new Intent(context, ViewAppointmentRequests.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Patient Reports")) {
                    Intent intent = new Intent(context, AddedPatientsActivity.class);
                    intent.putExtra("currentFlow", "AddReportActivity");
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("View Reports")) {
                    Intent intent = new Intent(context, AddReportsActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Analyze Symptoms")) {
                    Intent intent = new Intent(context, SymptomsActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Find Doctors")) {
                    Intent intent = new Intent(context, FindDoctor.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Chat Forum")) {
                    Intent intent = new Intent(context, DiscussionActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Awareness Information")) {
                    Intent intent = new Intent(context, InformationActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Messages")) {
                    Intent intent = new Intent(context, AddedPatientsActivity.class);
                    intent.putExtra("currentFlow", "Messages");
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Registered Patients")) {
                    Intent intent = new Intent(context, AddedPatientsActivity.class);
                    intent.putExtra("currentFlow", "Registered Patients");
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Medicines")) {
                    Intent intent = new Intent(context, PharmacistMedicinesActivity.class);
                    context.startActivity(intent);
                } else if (holder.title.getText().toString().equalsIgnoreCase("Order Requests")) {
                    Intent intent = new Intent(context, OrderRequestsActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dashboardList.size();
    }
}