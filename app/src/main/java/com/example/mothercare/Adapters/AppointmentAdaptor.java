package com.example.mothercare.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Appointment;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.AppointmentNotificationReciever;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AppointmentAdaptor extends RecyclerView.Adapter<AppointmentAdaptor.MyViewHolder> {
    private List<Appointment> appointmentList;
    public Context context;
    private FirebaseUtil firebaseUtil;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public AppointmentAdaptor(Context context, List<Appointment> appointments) {
        this.appointmentList = appointments;
        this.context = context;
        firebaseUtil = new FirebaseUtil(context);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Appointment appointment = appointmentList.get(position);
        holder.title.setText(appointment.getTitle());
        holder.date.setText("Date: " + appointment.getDate());
        holder.time.setText("Time: " + appointment.getTime());
        if (appointment.getStatus().equalsIgnoreCase("Completed")) {
            holder.appointmentOperations.setVisibility(View.GONE);
            holder.appointmentCompleted.setVisibility(View.VISIBLE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Appointments").child(firebaseUtil.getCurrentUserID()).child(appointment.getAppointmentID()).removeValue();
            }
        });
        holder.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCompleteAppointmentConfirmationDialog(view.getContext(), appointment, holder);
            }
        });
    }

    public void showCompleteAppointmentConfirmationDialog(final Context context, final Appointment appointment, final MyViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Appointment");
        //Setting message manually and performing action on button click
        builder.setMessage("Are you sure to complete this appointment? You won't get its notification on completion.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            firebaseUtil.completeAppointment(appointment.getAppointmentID());
                            Intent intent = new Intent(context, AppointmentNotificationReciever.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appointment.getNotificationID(), intent, 0);
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                            holder.appointmentOperations.setVisibility(View.GONE);
                            holder.appointmentCompleted.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Appointment Completed",
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Complete Appointment");
        alert.show();

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
        public TextView title, date, time;
        public ImageView completed, delete;
        public LinearLayout appointmentOperations, appointmentCompleted;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.appointmentHeading);
            date = (TextView) view.findViewById(R.id.appointmentDate);
            time = (TextView) view.findViewById(R.id.appointmentTime);
            completed = (ImageView) view.findViewById(R.id.completeAppointment);
            delete = (ImageView) view.findViewById(R.id.deleteAppointment);
            appointmentCompleted = (LinearLayout) view.findViewById(R.id.appointmentCompleted);
            appointmentOperations = (LinearLayout) view.findViewById(R.id.appointmentInProgress);
        }
    }
}
