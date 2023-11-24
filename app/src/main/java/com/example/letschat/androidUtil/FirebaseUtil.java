package com.example.letschat.androidUtil;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.SimpleTimeZone;

public class FirebaseUtil {
        public static boolean isLogined(){
            if(getCurrentUserId()!=null){
                return true;
            }
            return false;
        }
        public static String getCurrentUserId(){
            return FirebaseAuth.getInstance().getUid();
        }
        public static DocumentReference currentUserDetails(){
            return FirebaseFirestore.getInstance().collection("User").document(getCurrentUserId());
        }

        public static CollectionReference allUserCollectionReference(){
            return FirebaseFirestore.getInstance().collection("User");
        }
        public static DocumentReference getChatRoomReference(String chatRoomId){
            return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
        }
        public static String getChatRoomId(String userId1 ,String userId2){
            if(userId1.hashCode()<userId2.hashCode())
                return userId1+"_"+userId2;
            else
                return userId2+"_"+userId1;
        }

    public static CollectionReference getChatRoomMessageReference(String chatRoomId){
        return getChatRoomReference(chatRoomId).collection("chats");
    }
    public static CollectionReference allChatRoomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds){
            if(userIds.get(0).equals(FirebaseUtil.getCurrentUserId())){
                return allUserCollectionReference().document(userIds.get(1));
            }else {
                return allUserCollectionReference().document(userIds.get(0));
            }
        }
    public static String timestampToString(Timestamp timestamp){
            return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
    public static StorageReference getCurrentProfilePicStorage(){
            return FirebaseStorage.getInstance().getReference().child("profilePic")
                    .child(FirebaseUtil.getCurrentUserId());
    }

    public static StorageReference getOtherProfilePicStorage(String otherUserId){
        return FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(otherUserId);
    }
}
