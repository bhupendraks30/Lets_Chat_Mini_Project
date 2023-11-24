package com.example.letschat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.letschat.androidUtil.AndroidUtil;
import com.example.letschat.androidUtil.FirebaseUtil;
import com.example.letschat.modelUltil.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class Progile_Fragments extends Fragment {
    EditText profileUsername,profilePhone;
    ImageView profile;
    Button profileUpdateButton;
    ProgressBar progressBar;
    UserModel currentUser;
    TextView logout;

     public Progile_Fragments() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_progile__fragments, container, false);
        profile = view.findViewById(R.id.profile_image);
        profilePhone = view.findViewById(R.id.profile_phone);
        profileUsername= view.findViewById(R.id.profile_username);
        profileUpdateButton= view.findViewById(R.id.profile_update_button);
        progressBar= view.findViewById(R.id.progress_circular);
        logout = view.findViewById(R.id.profile_logout);

        getDataFromFirebase();

        profileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                getProgressBar(true);
                updateButtonClick();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getContext(), Splash_Screen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(getContext().getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(),Profile_Update_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }


    public void getDataFromFirebase(){
        getProgressBar(true);
        FirebaseUtil.getCurrentProfilePicStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri uri = task.getResult();
                    AndroidUtil.setProfileImage(getContext().getApplicationContext(),uri,profile);
                    getProgressBar(false);
                }
            }
        });
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    getProgressBar(false);
                    currentUser =task.getResult().toObject(UserModel.class);
                    profilePhone.setText(currentUser.getPhoneNumber());
                    profileUsername.setText(currentUser.getUsername());
                }
            }
        });

    }
    void updateButtonClick(){
        getProgressBar(false);
        String username_s = profileUsername.getText().toString();
        if(username_s.isEmpty()||username_s.length()<=3){
            profileUsername.setError("Username must be not null and grater then 3 character");
            return;
        }
        currentUser.setUsername(username_s);
        updateFirebase();
    }
    void updateFirebase(){
       FirebaseUtil.currentUserDetails()
               .set(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
             if (task.isSuccessful()){
                 Toast.makeText(getActivity().getApplicationContext(), "Update successful", Toast.LENGTH_SHORT).show();
             }else {
                 Toast.makeText(getActivity().getApplicationContext(), "Update successful", Toast.LENGTH_SHORT).show();
             }
           }
       });
    }

    public void getProgressBar(boolean visible) {
        if(visible){
            progressBar.setVisibility(View.VISIBLE);
            profileUpdateButton.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            profileUpdateButton.setVisibility(View.VISIBLE);
        }
    }
}