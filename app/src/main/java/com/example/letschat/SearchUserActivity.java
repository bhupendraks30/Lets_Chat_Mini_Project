package com.example.letschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.letschat.adapter.SearchUserRecyclerAdapter;
import com.example.letschat.androidUtil.FirebaseUtil;
import com.example.letschat.modelUltil.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {
    EditText usernameSearch;
    ImageButton back_button, search_btn;
    RecyclerView username_recyclerView;
    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        usernameSearch = findViewById(R.id.search_username_editText);
        back_button = findViewById(R.id.back_button);
        search_btn = findViewById(R.id.search_btn);
        username_recyclerView = findViewById(R.id.search_user_recyclerView);
        usernameSearch.requestFocus();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Main_Activity.class));
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameSearch.getText().toString();
                if(username.isEmpty()||username.length()<3){
                    usernameSearch.setError("Invalid Username");
                    return;
                }
                SearchUserRecyclerView(username);
            }
        });
    }
    void SearchUserRecyclerView(String username) {
        Query query = FirebaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username",username);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class)
                .build();

        adapter = new SearchUserRecyclerAdapter(options,getApplicationContext());
        username_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        username_recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }
}