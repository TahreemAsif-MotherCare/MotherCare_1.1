package com.example.mothercare.Views.Activities;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Adapters.DiscussionAdapter;
import com.example.mothercare.BaseActivity;
import com.example.mothercare.Enumeratios.FirebaseResponses;
import com.example.mothercare.Models.Chat;
import com.example.mothercare.Models.Patient;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientChatActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private DiscussionAdapter adapter;
    ArrayList<Chat> chatArrayList = new ArrayList<>();
    private RecyclerView chatRecyclerView;
    private EditText messageToSend;
    private ImageView sendMessage;
    private FirebaseUtil firebaseUtil;
    private String userName, doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat);

        firebaseUtil = new FirebaseUtil(this);
        getUserName();
        Intent intent = getIntent();
        doctorId = intent.getStringExtra("DoctorID");
        chatRecyclerView = findViewById(R.id.patientChatRecyclerView);
        messageToSend = findViewById(R.id.messageToSendPatientChat);
        sendMessage = findViewById(R.id.sendMessagePatientChat);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getChatMessages(doctorId, "Patient");

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (messageToSend.getText().toString().length() == 0) {
                    showToast("Empty Message cannot be send");
                } else {
                    Chat chat = new Chat(generateRandomID(), firebaseUtil.getCurrentUserID(), userName,
                            messageToSend.getText().toString(), Calendar.getInstance().getTime());
                    firebaseUtil.savePatientChatMessage(chat, doctorId);
                    chatArrayList.clear();
                    messageToSend.setText(null);
                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_patient_chat;
    }

    public void getUserName() {
        showHideProgress(true, "Please Wait");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patient").child(firebaseUtil.getCurrentUserID());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                userName = patient.username;
                showHideProgress(false, "Please Wait");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showHideProgress(false, "Please Wait");
                showAlertDialog("Error", databaseError.getMessage());
            }
        });
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        showHideProgress(false, "");
        switch (firebaseResponses) {
            case ChatMessages: {
                chatArrayList = (ArrayList<Chat>) o;
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mLayoutManager.scrollToPosition(chatArrayList.size() - 1);
                chatRecyclerView.setLayoutManager(mLayoutManager);
                chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new DiscussionAdapter(chatArrayList, this);
                chatRecyclerView.setAdapter(adapter);
            }
        }
    }
}
