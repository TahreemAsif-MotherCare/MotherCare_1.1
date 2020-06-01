package com.example.mothercare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Chat;
import com.example.mothercare.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.MyViewHolder> {
    private Context context;
    ArrayList<Chat> chatArrayList = new ArrayList<>();

    public DiscussionAdapter(ArrayList<Chat> arrayList, Context context) {
        this.context = context;
        this.chatArrayList = arrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = chatArrayList.get(position);
        if (chat.userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.senderUserName.setText(chat.userName);
            holder.senderChatMessage.setText(chat.chatMessage);
            holder.recieverLayout.setVisibility(View.GONE);
        } else {
            holder.recieverUserName.setText(chat.userName);
            holder.recieverChatMessage.setText(chat.chatMessage);
            holder.senderLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView senderUserName, senderChatMessage, recieverUserName, recieverChatMessage;
        LinearLayout senderLayout;
        RelativeLayout recieverLayout;

        public MyViewHolder(View view) {
            super(view);
            senderUserName = view.findViewById(R.id.chatUser);
            senderChatMessage = view.findViewById(R.id.chatMessage);
            recieverUserName = view.findViewById(R.id.chatUserReciever);
            recieverChatMessage = view.findViewById(R.id.chatMessageReciever);
            senderLayout = view.findViewById(R.id.senderLayout);
            recieverLayout = view.findViewById(R.id.recieverLayout);
        }
    }
}
