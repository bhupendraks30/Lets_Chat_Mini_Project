package com.example.letschat.modelUltil;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel {
    String chatRoomId;
    List<String> userId;
    Timestamp msgTimesMap;
    String lastMessageSenderId;
    String lastMessage;

    public ChatRoomModel() {
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public ChatRoomModel(String chatRoomId, List<String> userId, Timestamp msgTimesMap, String lastMessageSenderId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.msgTimesMap = msgTimesMap;
        this.lastMessageSenderId = lastMessageSenderId;
    }


    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public Timestamp getMsgTimesMap() {
        return msgTimesMap;
    }

    public void setMsgTimesMap(Timestamp msgTimesMap) {
        this.msgTimesMap = msgTimesMap;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }
}
