package com.dodo.veltech_leafscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {
    private ExtendedFloatingActionButton floatingActionButton;
    private EditText etDigit1, etDigit2, etDigit3, etDigit4;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mAuth = FirebaseAuth.getInstance();
        floatingActionButton = findViewById(R.id.buttonotp);

        final EditText inputMobile = findViewById(R.id.mobileno);
        Button buttonGetotp = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progressbar);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(login.this, "Enter Mobile", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + inputMobile.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        login.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String login_buttonId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(login_buttonId, forceResendingToken);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), Otp_screen.class);
                                        intent.putExtra("mobileno", inputMobile.getText().toString());
                                        intent.putExtra("login_buttonId", login_buttonId);
                                        startActivity(intent);
                                    }
                                }, 2000);


                            }
                        }
                );

            }
        });
    }
}