package com.example.mothercare.Views.Activities;

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

public class DiscussionActivity extends BaseActivity implements FirebaseUtil.FirebaseResponse {
    private DiscussionAdapter adapter;
    private RecyclerView chatRecyclerView;
    private EditText messageToSend;
    private ImageView sendMessage;
    private FirebaseUtil firebaseUtil;
    private String userName;
    ArrayList<Chat> chatArrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUtil = new FirebaseUtil(this);
        getUserName();

        chatRecyclerView = findViewById(R.id.discussionChatRecyclerView);
        messageToSend = findViewById(R.id.messageToSend);
        sendMessage = findViewById(R.id.sendMessage);
        firebaseUtil.setFirebaseResponse(this);
        showHideProgress(true, "");
        firebaseUtil.getDiscussionMessages();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageToSend.getText().toString().length() == 0) {
                    showToast("Empty Message cannot be send");
                } else {
                    Chat chat = new Chat(generateDiscussionID(), firebaseUtil.getCurrentUserID(), userName,
                            messageToSend.getText().toString(), Calendar.getInstance().getTime());
                    firebaseUtil.saveChatMessage(chat);
                    chatArrayList.clear();
                    messageToSend.setText(null);
                }
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
    protected int getLayoutResource() {
        return R.layout.activity_discussion;
    }

    @Override
    public void firebaseResponse(Object response, FirebaseResponses firebaseResponses) {
        showHideProgress(false, "");
        switch (firebaseResponses) {
            case DiscussionMessages: {
                chatArrayList = (ArrayList<Chat>) response;
                if (!chatArrayList.isEmpty()) {
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mLayoutManager.scrollToPosition(chatArrayList.size() - 1);
                    mLayoutManager.setSmoothScrollbarEnabled(true);
                    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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
