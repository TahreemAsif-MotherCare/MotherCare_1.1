package com.example.mothercare.Views.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.NotificationsAdapter;
import com.example.mothercare.Models.Notifications;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    ArrayList<Notifications> notificationsArrayList = new ArrayList<>();
    private FirebaseUtil firebaseUtil;
    private TextView noNotificationsFound;

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications2, container, false);
        firebaseUtil = new FirebaseUtil(getActivity());
        recyclerView = view.findViewById(R.id.notificationsRecyclerView);
        noNotificationsFound = view.findViewById(R.id.noNotificationsFound);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NotificationsAdapter(notificationsArrayList, getActivity());
        recyclerView.setAdapter(adapter);
        getNotifications();
        return view;
    }

    public void getNotifications() {
        notificationsArrayList.clear();
        adapter.notifyDataSetChanged();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Notifications").child(firebaseUtil.getCurrentUserID());
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Notifications notification = postSnapshot.getValue(Notifications.class);
                    notificationsArrayList.add(notification);
                }
                if (notificationsArrayList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    noNotificationsFound.setVisibility(View.VISIBLE);
                } else
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
