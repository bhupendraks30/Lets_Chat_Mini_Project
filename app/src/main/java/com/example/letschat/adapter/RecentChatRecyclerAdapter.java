package com.example.letschat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letschat.Chat_Activity;
import com.example.letschat.R;
import com.example.letschat.androidUtil.AndroidUtil;
import com.example.letschat.androidUtil.FirebaseUtil;
import com.example.letschat.modelUltil.ChatRoomModel;
import com.example.letschat.modelUltil.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatRecyclerAdapter.RecentChatViewHolder> {
    Context context;
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecentChatRecyclerAdapter.RecentChatViewHolder holder, int position, @NonNull ChatRoomModel model) {
        FirebaseUtil.getOtherUserFromChatroom(model.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    boolean x = model.getLastMessageSenderId().equals(FirebaseUtil.getCurrentUserId());

                    UserModel otherUserModel=task.getResult().toObject(UserModel.class);

                    FirebaseUtil.getOtherProfilePicStorage(otherUserModel.getUserId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri uri = task.getResult();
                                AndroidUtil.setProfileImage(context.getApplicationContext(),uri,holder.profile_picture);
                            }
                        }
                    });

                    holder.username_text.setText(otherUserModel.getUsername());
                    if(x){
                        holder.lastMessage_Text.setText("you : "+model.getLastMessage());
                    }else {
                        holder.lastMessage_Text.setText(model.getLastMessage());
                    }
                    holder.lastMessage_Time.setText(FirebaseUtil.timestampToString(model.getMsgTimesMap()));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Chat_Activity.class);
                            AndroidUtil.passUserModelAsIntent(intent,otherUserModel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    @NonNull
    @Override
    public RecentChatRecyclerAdapter.RecentChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_adapter_row,parent,false);
        return new RecentChatViewHolder(view);
    }

    public class RecentChatViewHolder extends RecyclerView.ViewHolder {
        TextView username_text;
        TextView lastMessage_Text;
        TextView lastMessage_Time;
        ImageView profile_picture;
        public RecentChatViewHolder(@NonNull View itemView) {
            super(itemView);
            username_text = itemView.findViewById(R.id.recent_chat_username_text);
            lastMessage_Text = itemView.findViewById(R.id.recent_chat_msg_text);
            lastMessage_Time = itemView.findViewById(R.id.recent_chat_time);
            profile_picture = itemView.findViewById(R.id.profile_picture);
        }
    }
}
