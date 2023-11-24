package com.example.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letschat.adapter.ChatRecyclerAdapter;
import com.example.letschat.adapter.SearchUserRecyclerAdapter;
import com.example.letschat.androidUtil.AndroidUtil;
import com.example.letschat.androidUtil.FirebaseUtil;
import com.example.letschat.modelUltil.ChatMessageModel;
import com.example.letschat.modelUltil.ChatRoomModel;
import com.example.letschat.modelUltil.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Chat_Activity extends AppCompatActivity {
    UserModel otherUser;
    ImageButton back_button,send_Button;
    TextView username;
    EditText msgBox;
    String chatRoomId;
    RecyclerView recyclerView;
    ChatRecyclerAdapter adapter;
    ChatRoomModel chatRoomModel;

    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatRoomId = FirebaseUtil.getChatRoomId(FirebaseUtil.getCurrentUserId(),otherUser.getUserId());

        back_button = findViewById(R.id.back_button_chatActivity);
        send_Button = findViewById(R.id.send_button_chatActivity);
        username = findViewById(R.id.username_chatActivity);
        msgBox = findViewById(R.id.msgBox_chatActivity);
        recyclerView =findViewById(R.id.recyclerView_ChatActivity);
        profile = findViewById(R.id.profile_picture);

        FirebaseUtil.getOtherProfilePicStorage(otherUser.getUserId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri uri = task.getResult();
                    AndroidUtil.setProfileImage(getApplicationContext(),uri,profile);
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        send_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msgBox.getText().toString().trim();
                if (message.isEmpty()){
                    return;
                }
                sendMessageToUser(message);
            }
        });
        username.setText(otherUser.getUsername());
        getOrCreateChatRoomModel();
        setupChatRecyclerView();
    }

    private void setupChatRecyclerView() {
        Query query = FirebaseUtil.getChatRoomMessageReference(chatRoomId)
                .orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class)
                .build();
        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager =new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void getOrCreateChatRoomModel() {
        FirebaseUtil.getChatRoomReference(chatRoomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    chatRoomModel=task.getResult().toObject(ChatRoomModel.class);
                    if (chatRoomModel==null){
                        chatRoomModel = new ChatRoomModel(chatRoomId,Arrays.asList(FirebaseUtil.getCurrentUserId(),otherUser.getUserId()),Timestamp.now(),"");
                        FirebaseUtil.getChatRoomReference(chatRoomId).set(chatRoomModel);
                    }
                }
            }
        });
    }

    void sendMessageToUser(String message) {
        chatRoomModel.setLastMessageSenderId(FirebaseUtil.getCurrentUserId());
        chatRoomModel.setMsgTimesMap(Timestamp.now());
        chatRoomModel.setLastMessage(message);
        FirebaseUtil.getChatRoomReference(chatRoomId).set(chatRoomModel);
        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtil.getCurrentUserId(),Timestamp.now());
        FirebaseUtil.getChatRoomMessageReference(chatRoomId).add(chatMessageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    msgBox.setText("");
                    sendNotification(message);
                }
            }
        });

    }


    private void sendNotification(String message) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    UserModel currentUser = task.getResult().toObject(UserModel.class);
                    try{
                        JSONObject jsonObject = new JSONObject();


                        JSONObject notification_obj = new JSONObject();
                        notification_obj.put("title",currentUser.getUsername());
                        notification_obj.put("body",message);

                        JSONObject dataObj = new JSONObject();
                        dataObj.put("userId",currentUser.getUserId());

                        jsonObject.put("notification",notification_obj);
                        jsonObject.put("data",dataObj);
                        jsonObject.put("to",otherUser.getFCMToken());

                        callApi(jsonObject);

                    }catch (Exception e){
                        Toast.makeText(Chat_Activity.this, "not sent", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    void callApi(JSONObject jsonObject){

        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String apiUrl = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .header("Authorization","Bearer AAAAaKyLjos:APA91bGlUhD02fEoGyfHtYJaCFlXWmLbi18lh9N9pEiKoLOqaatuh8fGWZDXD03T-R-2LB8RtKTEImv2es8J0IoamLO-Jxz3YjXvzyULxd6yHlH5_UFxXpqNhyhJPDXHNwLijKnqMOUf")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

}