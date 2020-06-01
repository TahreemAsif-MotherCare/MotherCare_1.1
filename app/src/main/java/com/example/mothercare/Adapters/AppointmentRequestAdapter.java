package com.example.mothercare.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Appointment;
import com.example.mothercare.Models.AppointmentRequest;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.AppointmentNotificationReciever;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class AppointmentRequestAdapter extends RecyclerView.Adapter<AppointmentRequestAdapter.MyViewHolder> {
    private ArrayList<AppointmentRequest> appointmentRequestArrayList;
    ArrayList<Patient> patients = new ArrayList<>();
    private Context context;
    private FirebaseUtil firebaseUtil;
    private int hour, minute, year, month, day;
    private String interval;
    private TextView datepickertextview;

    public AppointmentRequestAdapter(Context context, ArrayList<AppointmentRequest> appointmentRequests, ArrayList<Patient> patients) {
        this.appointmentRequestArrayList = appointmentRequests;
        this.context = context;
        this.patients = patients;
        hour = 0;
        minute = 0;
        year = 0;
        month = 0;
        day = 0;
        interval = null;
        firebaseUtil = new FirebaseUtil(this.context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appoint_requests_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final AppointmentRequest appointmentRequest = appointmentRequestArrayList.get(position);
        final Patient patient = patients.get(position);
        holder.title.setText(patient.username);
        holder.type.setText(appointmentRequest.appointmentType);
        holder.deleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.scheduleAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.add_appointment_dialog, null);
                final EditText appointmentTitle = dialogView.findViewById(R.id.appointmentName);

                Button addAppointment = dialogView.findViewById(R.id.addappointmentSchedule);
                Button dismissAppointmentDialog = dialogView.findViewById(R.id.cancel_actionAppointment);
                datepickertextview = (TextView) dialogView.findViewById(R.id.datepickertextviewAppointment);
                final TextView timepickertextview = (TextView) dialogView.findViewById(R.id.timepickertextviewAppointment);


                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                java.util.Calendar cal = java.util.Calendar.getInstance();
                addAppointment.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("WrongConstant")
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        if (appointmentTitle.getText().toString().isEmpty()) {
                            appointmentTitle.setFocusable(true);
                            appointmentTitle.setError("Title cannot be empty!");
                        } else if (timepickertextview.getText().toString().contains("Time")) {
                            timepickertextview.setFocusable(true);
                            timepickertextview.setError("Invalid time!");
                        } else if (datepickertextview.getText().toString().contains("Date")) {
                            timepickertextview.setFocusable(true);
                            timepickertextview.setError("Invalid date!");
                        } else {
                            String appointmentID = generateAppointmentID();
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
                                startAlarm(c, appointmentID, notificationID);
                            }
                            prepareAppointment(appointmentID, appointmentTitle.getText().toString(),
                                    datepickertextview.getText().toString(), appointmentRequest.appointmentType, timepickertextview.getText().toString(), notificationID,
                                    patient, appointmentRequest);
                            appointmentRequestArrayList.remove(position);

                        }
                    }
                });
                dismissAppointmentDialog.setOnClickListener(new View.OnClickListener() {
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
                        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
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
    public int getItemCount() {
        return appointmentRequestArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, type;
        public Button deleteRequest, scheduleAppointment;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.patientNameAppointmentRequest);
            type = view.findViewById(R.id.appointmentTypeRequestRow);
            deleteRequest = view.findViewById(R.id.deleteAppointmentRequest);
            scheduleAppointment = view.findViewById(R.id.scheduleAppointment);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startAlarm(Calendar c, String appointmentID, int notificationID) {
        Random random = new Random();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AppointmentNotificationReciever.class);
        intent.putExtra("notificationChannelID", appointmentID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationID, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public String generateAppointmentID() {
        String AlphaNumericString = "0123456789";
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i < 15; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    private int generateNotificationID() {
        Random r = new Random();
        int low = 10;
        int high = 1000000000;
        return r.nextInt(high - low) + low;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void chooseDate() {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
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

    private void prepareAppointment(String AppointmentID, String title, String date, String appointmentType, String time, int notificationID,
                                    Patient patient, AppointmentRequest appointmentRequest) {
        Appointment appointment = new Appointment(AppointmentID, title, date, time, appointmentType, notificationID, "Incompleted", firebaseUtil.getCurrentUserID(),
                patient.patientID);
        storeAppointmentinFirebase(appointment, appointmentRequest);
    }

    private void storeAppointmentinFirebase(Appointment appointment, AppointmentRequest appointmentRequest) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        try {
            final DatabaseReference appointmentRef = firebaseDatabase.getReference().child("Appointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(appointment.getAppointmentID());
            appointmentRef.setValue(appointment);
            firebaseUtil.deleteAppointmentRequest(appointmentRequest);
            notifyDataSetChanged();
        } catch (Exception e) {
            Log.d("Appointment", e.getMessage());
        }
    }
}
