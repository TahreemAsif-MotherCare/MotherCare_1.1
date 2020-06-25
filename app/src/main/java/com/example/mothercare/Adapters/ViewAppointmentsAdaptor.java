package com.example.mothercare.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Appointment;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        try {
            SimpleDateFormat formatter6 = new SimpleDateFormat("dd-mm-YYYY hh:mm");
            Date appointmentDate = formatter6.parse(appointment.getDate() + " " + appointment.getTime());
            Date currentTime = Calendar.getInstance().getTime();

            if (appointmentDate.compareTo(currentTime) < 0) {
                if (appointmentDate.getYear() <= currentTime.getYear()) {
                    if (appointmentDate.getMonth() + 1 <= currentTime.getMonth() + 1) {
                        if (appointmentDate.getDate() < currentTime.getDate()) {
                            if (holder.appointmentIcon.getVisibility() == View.GONE) {
                                holder.appointmentIcon.setVisibility(View.VISIBLE);
                            }
                            if (appointment.getAppointmentType().equals("Video Call")) {
                                holder.appointmentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_call));
                            } else if (appointment.getAppointmentType().equals("Call")) {
                                holder.appointmentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_call));
                            }
                            holder.appointmentIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                        } else {
                            if (holder.appointmentIcon.getVisibility() == View.GONE) {
                                holder.appointmentIcon.setVisibility(View.VISIBLE);
                            }
                            if (appointment.getAppointmentType().equals("Video Call")) {
                                holder.appointmentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_call));
                            } else if (appointment.getAppointmentType().equals("Call")) {
                                holder.appointmentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_call));
                            }
                            holder.appointmentIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                        }
                    } else {
                        if (holder.appointmentIcon.getVisibility() == View.GONE) {
                            holder.appointmentIcon.setVisibility(View.VISIBLE);
                        }
                        if (appointment.getAppointmentType().equals("Video Call")) {
                            holder.appointmentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_call));
                        } else if (appointment.getAppointmentType().equals("Call")) {
                            holder.appointmentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_call));
                        }
                        holder.appointmentIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    if (appointment.getAppointmentType().equals("Video Call")) {
                        holder.appointmentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_call));
                    } else if (appointment.getAppointmentType().equals("Call")) {
                        holder.appointmentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_call));
                    }
                    holder.appointmentIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            } else {
                holder.appointmentIcon.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.title.setText(appointment.getTitle());
        holder.type.setText(appointment.getAppointmentType());
        holder.date.setText(appointment.getDate());
        holder.time.setText(appointment.getTime());

        holder.appointmentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String contactNumber = "03125779969"; // to change with real value
                Cursor cursor = context.getContentResolver()
                        .query(
                                ContactsContract.Data.CONTENT_URI,
                                new String[]{ContactsContract.Data._ID},
                                ContactsContract.RawContacts.ACCOUNT_TYPE + " = 'com.whatsapp' " +
                                        "AND " + ContactsContract.Data.MIMETYPE + " = 'vnd.android.cursor.item/vnd.com.whatsapp.video.call' " +
                                        "AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + contactNumber + "%'",
                                null,
                                ContactsContract.Contacts.DISPLAY_NAME
                        );
                if (cursor == null) {
                    // throw an exception
                }
                long id = -1;
                while (cursor.moveToNext()) {
                    id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);

                intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + id), "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                intent.setPackage("com.whatsapp");

                context.startActivity(intent);*/
            }
        });
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
        public ImageView appointmentIcon;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.viewAppointmentHeading);
            type = (TextView) view.findViewById(R.id.appointmentType);
            date = (TextView) view.findViewById(R.id.viewAppointmentDate);
            time = (TextView) view.findViewById(R.id.viewAppointmentTime);
            appointmentIcon = (ImageView) view.findViewById(R.id.appointmentIcon);

        }
    }
}
