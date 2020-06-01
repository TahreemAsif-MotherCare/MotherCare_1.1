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
import com.example.mothercare.Models.Doctor;
import com.example.mothercare.R;
import com.example.mothercare.Utilities.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorChatActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private DiscussionAdapter adapter;
    private RecyclerView chatRecyclerView;
    private EditText messageToSend;
    private ImageView sendMessage;
    private FirebaseUtil firebaseUtil;
    ArrayList<Chat> chatArrayList = new ArrayList<>();
    private String userName, patientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseUtil = new FirebaseUtil(this);
        getUserName();
        Intent intent = getIntent();
        patientID = intent.getStringExtra("PatientID");
        chatRecyclerView = findViewById(R.id.doctorChatRecyclerView);
        messageToSend = findViewById(R.id.messageToSendDoctorChat);
        sendMessage = findViewById(R.id.sendMessageDoctorChat);
        firebaseUtil.setFirebaseResponse(this);
        firebaseUtil.getChatMessages(patientID, "DoctorChat");

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (messageToSend.getText().toString().length() == 0) {
                    showToast("Empty Message cannot be send");
                } else {
                    Chat chat = new Chat(generateDiscussionID(), firebaseUtil.getCurrentUserID(), userName,
                            messageToSend.getText().toString(), Calendar.getInstance().getTime());
                    firebaseUtil.saveDoctorChatMessage(chat, patientID);
                    chatArrayList.clear();
                    messageToSend.setText(null);
                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_doctor_chat;
    }

    public void getUserName() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors").child(firebaseUtil.getCurrentUserID());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                userName = doctor.username;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showAlertDialog("Error", databaseError.getMessage());
            }
        });
    }

    public String generateDiscussionID() {
        String AlphaNumericString = "0123456789";
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i < 15; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    @Override
    public void firebaseResponse(Object o, FirebaseResponses firebaseResponses) {
        switch (firebaseResponses) {
            case ChatMessages: {
                chatArrayList = (ArrayList<Chat>) o;
                if (!chatArrayList.isEmpty()) {
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mLayoutManager.scrollToPosition(chatArrayList.size() - 1);
                    chatRecyclerView.setLayoutManager(mLayoutManager);
                    chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter = new DiscussionAdapter(chatArrayList, this);
                    chatRecyclerView.setAdapter(adapter);
                }
                break;
            }
            case Error:
        }
    }
}