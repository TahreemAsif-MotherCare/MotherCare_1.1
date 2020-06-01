package com.example.mothercare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Notifications;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private ArrayList<Notifications> notifications;
    private FirebaseUtil firebaseUtil;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitle, notificationDesc;

        public MyViewHolder(View view) {
            super(view);
            notificationTitle = view.findViewById(R.id.notificationTitle);
            notificationDesc = view.findViewById(R.id.notificationDescription);
        }
    }


    public NotificationsAdapter(ArrayList<Notifications> notificationsArrayList, Context context) {
        this.notifications = notificationsArrayList;
        this.context = context;
        firebaseUtil = new FirebaseUtil(context);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Notifications notification = notifications.get(position);
        holder.notificationTitle.setText(notification.getNotificationTitle());
        holder.notificationDesc.setText(notification.getTime());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}