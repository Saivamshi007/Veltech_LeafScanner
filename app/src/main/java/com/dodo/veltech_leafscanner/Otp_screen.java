package com.dodo.veltech_leafscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otp_screen extends AppCompatActivity {
    EditText etDigit1,etDigit2,etDigit3,etDigit4,etDigit5,etDigit6;
    private String verificationid;
    private TextView mobiletext;
    private TextView resend;
    private TextView Sending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);

        etDigit1=findViewById(R.id.etDigit1);
        etDigit2=findViewById(R.id.etDigit2);
        etDigit3=findViewById(R.id.etDigit3);
        etDigit4=findViewById(R.id.etDigit4);
        etDigit5 = findViewById(R.id.etDigit5);
        etDigit6 = findViewById(R.id.etDigit6);
        mobiletext = findViewById(R.id.mobiletext);
        resend = findViewById(R.id.notrecievecode);
        Sending = findViewById(R.id.sending);

        stupOtps();
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button button = findViewById(R.id.btnsubmit);
        verificationid = getIntent().getStringExtra("login_buttonId");
        Toast.makeText(this, "id"+verificationid, Toast.LENGTH_SHORT).show();
        String mobilno  = getIntent().getStringExtra("mobileno");
        mobiletext.setText(mobilno);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etDigit1.getText().toString().trim().isEmpty()||
                        etDigit2.getText().toString().trim().isEmpty()||
                        etDigit3.getText().toString().trim().isEmpty()||
                        etDigit4.getText().toString().trim().isEmpty()||
                        etDigit5.getText().toString().trim().isEmpty()||
                        etDigit6.getText().toString().trim().isEmpty()){
                    Toast.makeText(Otp_screen.this, "Please enter valid otp", Toast.LENGTH_LONG).show();
                    return;
                }
                String code = etDigit1.getText().toString() +
                        etDigit2.getText().toString() +
                        etDigit3.getText().toString() +
                        etDigit4.getText().toString() +
                        etDigit5.getText().toString() +
                        etDigit6.getText().toString();
                if(verificationid != null){
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(

                            verificationid,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);

                            if(task.isSuccessful()){
                                boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();



                                if(newuser){
                                    Intent intent = new Intent(Otp_screen.this,user_role.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();


                                }else{
                                    Intent intent = new Intent(Otp_screen.this,MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }

                            }else{
                                Toast.makeText(Otp_screen.this, "Entered Verification Code is wrong! please check it ", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sending.setVisibility(View.VISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobileno"),
                        60,
                        TimeUnit.SECONDS,
                        Otp_screen.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Sending.setVisibility(View.GONE);
                                Toast.makeText(Otp_screen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newverificationid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Sending.setVisibility(View.GONE);
                                verificationid = newverificationid;

                                Toast.makeText(Otp_screen.this, "OTP sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

            }
        });

    }

    private void stupOtps() {
        etDigit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    etDigit2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){
                    etDigit1.requestFocus();
                }
            }
        });
        etDigit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    etDigit3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){
                    etDigit1.requestFocus();
                }
            }
        });
        etDigit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    etDigit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){
                    etDigit2.requestFocus();
                }
            }
        });
        etDigit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    etDigit5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){
                    etDigit3.requestFocus();
                }
            }
        });
        etDigit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    etDigit6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){
                    etDigit4.requestFocus();
                }
            }
        });
        etDigit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    etDigit6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){
                    etDigit5.requestFocus();
                }
            }
        });
    }
}