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
import com.example.letschat.modelUltil.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel,SearchUserRecyclerAdapter.UserModelViewHolder> {
    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
            holder.username.setText(model.getUsername());
            holder.phone.setText(model.getPhoneNumber());
            if (model.getUserId().toString().equals(FirebaseUtil.getCurrentUserId().toString())){
                holder.username.setText(model.getUsername()+" (Me)");
            }

            FirebaseUtil.getOtherProfilePicStorage(model.getUserId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtil.setProfileImage(context.getApplicationContext(),uri,holder.profile);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Chat_Activity.class);
                    AndroidUtil.passUserModelAsIntent(intent,model);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView phone;
        ImageView profile;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_text);
            phone = itemView.findViewById(R.id.user_phone_text);
            profile = itemView.findViewById(R.id.profile_picture);
        }
    }
}
