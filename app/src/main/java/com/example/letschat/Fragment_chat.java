package com.example.letschat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letschat.adapter.RecentChatRecyclerAdapter;
import com.example.letschat.adapter.SearchUserRecyclerAdapter;
import com.example.letschat.androidUtil.FirebaseUtil;
import com.example.letschat.modelUltil.ChatRoomModel;
import com.example.letschat.modelUltil.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class Fragment_chat extends Fragment {
    RecyclerView recyclerView;
    RecentChatRecyclerAdapter adapter;


    public Fragment_chat() {
        // Required empty public constructor
    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_Chatscreen);
        getRecentChatRecyclerAdapter();
        return view;
    }

    void getRecentChatRecyclerAdapter() {
        Query query = FirebaseUtil.allChatRoomCollectionReference()
                .whereArrayContains("userId", FirebaseUtil.getCurrentUserId())
                .orderBy("msgTimesMap", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query,ChatRoomModel.class)
                .build();
        adapter = new RecentChatRecyclerAdapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }
    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }
}