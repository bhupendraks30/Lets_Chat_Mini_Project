package com.example.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.letschat.androidUtil.AndroidUtil;
import com.example.letschat.androidUtil.FirebaseUtil;
import com.example.letschat.modelUltil.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class Login_UserName_Activity extends AppCompatActivity {
    EditText username;
    String phoneNumber;
    UserModel userModel;
    ImageView profile;
    ProgressBar progressBar;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);
        username = findViewById(R.id.loginUsername);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        profile = findViewById(R.id.progile_login_username);
        progressBar= findViewById(R.id.progressBar_login_username);
        loginButton = findViewById(R.id.loginButton_loging_user);
        getUsername();
        getProgressBar(false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProgressBar(true);
                setUsername();
            }
        });
    }

    void setUsername(){
        String username_s = username.getText().toString();
        if(username_s.isEmpty()||username_s.length()<=3){
            getProgressBar(false);
            username.setError("Username must be not null and grater then 3 character");
            return;
        }
        if(userModel!=null){
            userModel.setUsername(username_s);
        }else{
            userModel = new UserModel(username_s,phoneNumber, Timestamp.now(),FirebaseUtil.getCurrentUserId());
        }
        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful()){
                  getProgressBar(false);
                  Intent i = new Intent(getApplicationContext(),Main_Activity.class);
                  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(i);
              }
            }
        });

    }

    void getUsername() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    userModel =task.getResult().toObject(UserModel.class);
                    if (userModel!=null){
                        getProgressBar(false);
                        username.setText(userModel.getUsername());
                        FirebaseUtil.getCurrentProfilePicStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                               if (task.isSuccessful()){
                                   Uri uri = task.getResult();
                                   if (uri!=null){
                                       AndroidUtil.setProfileImage(getApplication().getApplicationContext(),uri,profile);
                                   }
                               }
                            }
                        });
                    }
                }
            }
        });
    }

    public void getProgressBar(boolean visible) {
        if(visible){
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }
}