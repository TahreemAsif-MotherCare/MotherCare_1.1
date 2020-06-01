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

import com.example.mothercare.Models.PatientMedicalHistory;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Utilities.MedicationNotificationReciever;
import com.example.mothercare.Views.Activities.MedicineRemindersActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PatientMedicalHistoryAdaptor extends RecyclerView.Adapter<PatientMedicalHistoryAdaptor.MyViewHolder> {
    private List<PatientMedicalHistory> patientMedicalHistoryList;
    private Context context;
    private FirebaseUtil firebaseUtil;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_medicine_reminder_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public PatientMedicalHistoryAdaptor(Context context, List<PatientMedicalHistory> patientMedicalHistories) {
        this.context = context;
        this.patientMedicalHistoryList = patientMedicalHistories;
        firebaseUtil = new FirebaseUtil(((MedicineRemindersActivity) context));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final PatientMedicalHistory patientMedicalHistory = patientMedicalHistoryList.get(position);
        holder.title.setText(patientMedicalHistory.getTitle());
        holder.description.setText("Medicine schedule is set on: ");
        holder.reminderTime.setText(patientMedicalHistory.getTime());
        holder.reminderDate.setText(patientMedicalHistory.getDate());
        if (patientMedicalHistory.getStatus().equalsIgnoreCase("Completed")) {
            holder.medicationOperations.setVisibility(View.GONE);
            holder.medicationCompleted.setVisibility(View.VISIBLE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("MedicationSchedule").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(patientMedicalHistory.getMedicationID()).removeValue();
                Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_LONG).show();
            }
        });
        holder.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCompleteMedicationConfirmationDialog(view.getContext(), patientMedicalHistory, holder);
            }
        });
    }

    public void showCompleteMedicationConfirmationDialog(final Context context, final PatientMedicalHistory patientMedicalHistory, final MyViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Medication Schedule");
        builder.setMessage("Are you sure to complete this medication schedule ? You won't get its notification on completion.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            firebaseUtil.completMedicationSchedule(firebaseUtil.getCurrentUserID(), patientMedicalHistory.getMedicationID());
                            Intent intent = new Intent(context, MedicationNotificationReciever.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, patientMedicalHistory.getNotificationID(), intent, 0);
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                            holder.medicationOperations.setVisibility(View.GONE);
                            holder.medicationCompleted.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Medication Schedule Completed",
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("AlertDialogExample");
        alert.show();

    }

    @Override
    public int getItemCount() {
        return patientMedicalHistoryList.size();
    }

    public void setPatientMedicalHistoryList(List<PatientMedicalHistory> patientMedicalHistories) {
        this.patientMedicalHistoryList = patientMedicalHistories;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, reminderDate, reminderTime;
        public ImageView completed, delete;
        public LinearLayout medicationOperations, medicationCompleted;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.patientMedicalHistoryHeading);
            description = (TextView) view.findViewById(R.id.patientMedicalHistoryDescription);
            reminderDate = (TextView) view.findViewById(R.id.reminderDate);
            reminderTime = (TextView) view.findViewById(R.id.reminderTime);
            completed = (ImageView) view.findViewById(R.id.completeMedication);
            delete = (ImageView) view.findViewById(R.id.deleteMedication);
            medicationOperations = (LinearLayout) view.findViewById(R.id.medicationOperation);
            medicationCompleted = (LinearLayout) view.findViewById(R.id.medicationCompleted);
        }
    }
}