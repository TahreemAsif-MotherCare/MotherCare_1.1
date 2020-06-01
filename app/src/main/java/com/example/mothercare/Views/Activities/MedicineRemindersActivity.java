package com.example.mothercare.Views.Activities;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.PatientMedicalHistoryAdaptor;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.PatientMedicalHistory;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.example.mothercare.Utilities.MedicationNotificationReciever;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class MedicineRemindersActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private ArrayList<PatientMedicalHistory> patientMedicalHistoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton addReminder;
    private TextView datepickertextview, timepickertextview, noMedicationSchedule;
    private PatientMedicalHistoryAdaptor mAdapter;
    private int hour, minute, year, month, day;
    private String interval;
    private FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Medication Schedule");
        recyclerView = findViewById(R.id.patient_medical_history_recyclerView);
        noMedicationSchedule = findViewById(R.id.noMedicationSchedule);
        addReminder = findViewById(R.id.addReminder);
        init();
        patientMedicalHistoryList.clear();
        firebaseUtil.getMedicationHistory();

        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MedicineRemindersActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.add_medicine_reminder_dialog, null);
                final EditText medicineName = dialogView.findViewById(R.id.medicineName);
                Button addMedicationScheduleDialog = dialogView.findViewById(R.id.addMedicineScheduleDialog);
                Button dismissMedicationScheduleDialog = dialogView.findViewById(R.id.cancel_action);
                datepickertextview = (TextView) dialogView.findViewById(R.id.datepickertextview);
                timepickertextview = (TextView) dialogView.findViewById(R.id.timepickertextview);

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                addMedicationScheduleDialog.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("WrongConstant")
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        if (medicineName.getText().toString().isEmpty()) {
                            medicineName.setFocusable(true);
                            medicineName.setError("Title cannot be empty!");
                        } else if (timepickertextview.getText().toString().contains("Time")) {
                            timepickertextview.setFocusable(true);
                            timepickertextview.setError("Invalid time!");
                        } else {
                            String medicationID = generateRandomID();
                            int notificationID = generateNotificationID();
                            dialog.dismiss();
                            Calendar c = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                c = Calendar.getInstance();
                                splitDate();

                                c.clear();
                                c.set(Calendar.DAY_OF_MONTH, day);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.HOUR_OF_DAY, hour);
                                c.set(Calendar.MINUTE, minute);
                                c.set(Calendar.SECOND, 0);

                                c.add(Calendar.MONTH, -1);
                                startAlarm(c, medicationID, notificationID, medicineName.getText().toString());
                            }
                            prepareMedicationSchedule(medicationID, medicineName.getText().toString(),
                                    datepickertextview.getText().toString(), timepickertextview.getText().toString(), notificationID);
                        }
                    }
                });
                dismissMedicationScheduleDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                timepickertextview.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final Calendar c = Calendar.getInstance();
                        hour = c.get(Calendar.HOUR_OF_DAY);
                        minute = c.get(Calendar.MINUTE);

                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(MedicineRemindersActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                Calendar mcurrentTime = Calendar.getInstance();
                                hour = selectedHour;
                                minute = selectedMinute;
                                timepickertextview.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, false);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();

                    }
                });
                datepickertextview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            chooseDate();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_medication;
    }

    private void prepareMedicationSchedule(String MedicationID, String title, String date, String time, int notificationID) {
        PatientMedicalHistory patientMedicalHistory = new PatientMedicalHistory(MedicationID, title, date, time, notificationID, "Incompleted");
        storeMedicationScheduleinFirebase(patientMedicalHistory);
    }

    private void storeMedicationScheduleinFirebase(PatientMedicalHistory medicalHistory) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        try {
            patientMedicalHistoryList.clear();
            final DatabaseReference medicationRef = firebaseDatabase.getReference().child("MedicationSchedule").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(medicalHistory.getMedicationID());
            medicationRef.setValue(medicalHistory);

        } catch (Exception e) {
            Log.d("Medication Schedule", e.getMessage());
        }
    }


    private void init() {
        firebaseUtil = new FirebaseUtil(MedicineRemindersActivity.this);
        firebaseUtil.setFirebaseResponse(MedicineRemindersActivity.this);
        hour = 0;
        minute = 0;
        year = 0;
        month = 0;
        day = 0;
        interval = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startAlarm(Calendar c, String medicationID, int notificationID, String medicationName) {
        Random random = new Random();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MedicationNotificationReciever.class);
        intent.putExtra("notificationChannelID", medicationID);
        intent.putExtra("notificationID", notificationID);
        intent.putExtra("Medication Name", medicationName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationID, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void chooseDate() {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        datepickertextview.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void splitDate() {
        String date = datepickertextview.getText().toString();
        String[] dateArray = date.split("-", 3);
        day = Integer.parseInt(dateArray[0]);
        month = Integer.parseInt(dateArray[1]);
        year = Integer.parseInt(dateArray[2]);
    }

    public void setRecyclerView() {
        mAdapter = new PatientMedicalHistoryAdaptor(MedicineRemindersActivity.this, patientMedicalHistoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        showHideProgress(false, "");
        switch (firebaseResponses) {
            case MedicineReminders: {
                patientMedicalHistoryList = (ArrayList<PatientMedicalHistory>) o;
                setRecyclerView();
            }
            case Error:
        }
    }
}
