package com.example.letschat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letschat.R;
import com.example.letschat.androidUtil.FirebaseUtil;
import com.example.letschat.modelUltil.ChatMessageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.api.Distribution;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel,ChatRecyclerAdapter.ChatModelViewHolder> {
    Context context;

    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options,Context context) {
        super(options);
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        if (model.getSenderId().equals(FirebaseUtil.getCurrentUserId())){
            holder.leftChat.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.rightChatMessage.setText(model.getMessage());
        }else{
            holder.rightChat.setVisibility(View.GONE);
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.leftChatMessage.setText(model.getMessage());
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recyclerview_row,parent,false);
        return new ChatRecyclerAdapter.ChatModelViewHolder(view);
    }

    public class ChatModelViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rightChat,leftChat;
        TextView rightChatMessage,leftChatMessage;
        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            rightChat = itemView.findViewById(R.id.right_chat);
            leftChat = itemView.findViewById(R.id.left_chat);
            rightChatMessage =itemView.findViewById(R.id.right_chat_message);
            leftChatMessage = itemView.findViewById(R.id.left_chat_message);
        }
    }
}
