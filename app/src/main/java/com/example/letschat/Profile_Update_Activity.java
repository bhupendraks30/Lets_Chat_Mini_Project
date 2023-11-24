package com.example.letschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.letschat.androidUtil.AndroidUtil;
import com.example.letschat.androidUtil.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class Profile_Update_Activity extends AppCompatActivity {

    ImageView imageView;
    Button updateImage;
    ProgressBar progressBar;
    Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        imageView = findViewById(R.id.imageView_profile_update);
        updateImage = findViewById(R.id.update_button_profile_update);
        progressBar = findViewById(R.id.progress_update_image);
        getProgressBar(false);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext()).withPermissions(android.Manifest.permission.READ_MEDIA_IMAGES)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Browse for image"),1);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProgressBar(true);
                if (filePath!=null){

                    FirebaseUtil.getCurrentProfilePicStorage().putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                getProgressBar(false);
                                Toast.makeText(Profile_Update_Activity.this, "Uploaded Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                getProgressBar(false);
                                Toast.makeText(Profile_Update_Activity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    getProgressBar(false);
                    Toast.makeText(Profile_Update_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1  && resultCode==RESULT_OK){
            filePath = data.getData();
            AndroidUtil.setProfileImage(getApplicationContext(),filePath,imageView);
        }
    }

    public void getProgressBar(boolean visible) {
        if(visible){
            progressBar.setVisibility(View.VISIBLE);
            updateImage.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            updateImage.setVisibility(View.VISIBLE);
        }
    }

}