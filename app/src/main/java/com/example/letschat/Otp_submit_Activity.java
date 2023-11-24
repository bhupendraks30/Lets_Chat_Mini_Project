package com.example.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Otp_submit_Activity extends AppCompatActivity {
    EditText otp_number;
    Timer timer;
    String verificationCode;
    Long timeOut=60L;
    PhoneAuthProvider.ForceResendingToken resentToken;
    TextView resent_otp;
    Button next_step;
    FirebaseAuth auth;
    String phoneNumber;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_submit);
        otp_number = findViewById(R.id.loginOtpEditText);
        resent_otp = findViewById(R.id.resend_otp);
        next_step = findViewById(R.id.loginNextButton);
        progressBar = findViewById(R.id.progressBar_otp_submit);
        getProgressBar(true);
        auth = FirebaseAuth.getInstance();
        Intent i = getIntent();
        phoneNumber = i.getStringExtra("PhoneNumber");
        sentOtp(phoneNumber,false);
        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProgressBar(true);
                String enteredOtp = otp_number.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
                verifyOtp(credential);
            }
        });

        resent_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentOtp(phoneNumber,true);
            }
        });

    }

    void sentOtp(String phoneNumber, boolean resend_otp){
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeOut, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        verifyOtp(phoneAuthCredential);
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        getProgressBar(false);
                        Toast.makeText(Otp_submit_Activity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        resentToken = forceResendingToken;
                        getProgressBar(false);
                        Toast.makeText(Otp_submit_Activity.this, "OTP send successfully to "+phoneNumber, Toast.LENGTH_SHORT).show();
                    }
                });
        if(resend_otp){
            getProgressBar(true);
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resentToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void verifyOtp(PhoneAuthCredential phoneAuthCredential){
        auth.signInWithCredential(phoneAuthCredential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Otp_submit_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Login_UserName_Activity.class).putExtra("phoneNumber",phoneNumber));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getProgressBar(false);
                        Toast.makeText(Otp_submit_Activity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getProgressBar(boolean visible) {
        if(visible){
            progressBar.setVisibility(View.VISIBLE);
            next_step.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            next_step.setVisibility(View.VISIBLE);
        }
    }


}