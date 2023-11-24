package com.example.letschat.androidUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.letschat.modelUltil.UserModel;

public class AndroidUtil {

    public static void passUserModelAsIntent(Intent intent , UserModel model){
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phoneNumber",model.getPhoneNumber());
        intent.putExtra("userId",model.getUserId());
        intent.putExtra("fcmToken",model.getFCMToken());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel model = new UserModel();
        model.setUsername(intent.getStringExtra("username"));
        model.setUserId(intent.getStringExtra("userId"));
        model.setPhoneNumber(intent.getStringExtra("phoneNumber"));
        model.setFCMToken(intent.getStringExtra("fcmToken"));
        return model;
    }
    public static void setProfileImage(Context context, Uri uri, ImageView imageView){
        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    public static void setProfileImage(Context context, Bitmap bitmap, ImageView imageView){
        Glide.with(context).load(bitmap).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

}
