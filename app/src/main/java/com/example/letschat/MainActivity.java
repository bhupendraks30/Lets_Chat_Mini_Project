package com.example.letschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    EditText P_number;
    Intent i;
    Button send_Otp;
    String s;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i = new Intent(this, Otp_submit_Activity.class);
        ccp = findViewById(R.id.lccp);
        P_number = findViewById(R.id.loginPhoneNumber);
        send_Otp = findViewById(R.id.loginSendOtp);
        progressBar = findViewById(R.id.progressbar_mainActivity);

        getProgressBar(false);

        ccp.registerCarrierNumberEditText(P_number);
        send_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProgressBar(true);
                try {
                    if(!ccp.isValidFullNumber()){
                        getProgressBar(false);
                        Toast.makeText(MainActivity.this, "Invalied Number", Toast.LENGTH_SHORT).show();
                        P_number.setText(null);
                        return;
                    }
                    i.putExtra("PhoneNumber",ccp.getFullNumberWithPlus());
                    startActivity(i);
                    finish();
                }catch (Exception e){
                    getProgressBar(false);
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
    }

    public void getProgressBar(boolean visible) {
        if(visible){
            progressBar.setVisibility(View.VISIBLE);
            send_Otp.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            send_Otp.setVisibility(View.VISIBLE);
        }
    }

}