package com.example.mothercare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Appointment;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class ViewAppointmentsAdaptor extends RecyclerView.Adapter<ViewAppointmentsAdaptor.MyViewHolder> {
    private List<Appointment> appointmentList = new ArrayList<>();
    public Context context;
    private FirebaseUtil firebaseUtil;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_appointments_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public ViewAppointmentsAdaptor(Context context, List<Appointment> appointments) {
        this.appointmentList = appointments;
        this.context = context;
        firebaseUtil = new FirebaseUtil(context);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Appointment appointment = appointmentList.get(position);
        holder.title.setText(appointment.getTitle());
        holder.type.setText(appointment.getAppointmentType());
        holder.date.setText(appointment.getDate());
        holder.time.setText(appointment.getTime());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, type, date, time;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.viewAppointmentHeading);
            type = (TextView) view.findViewById(R.id.appointmentType);
            date = (TextView) view.findViewById(R.id.viewAppointmentDate);
            time = (TextView) view.findViewById(R.id.viewAppointmentTime);

        }
    }
}
