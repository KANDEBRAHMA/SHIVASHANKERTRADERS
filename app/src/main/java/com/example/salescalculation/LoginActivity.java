package com.example.salescalculation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText PhoneNumber,etOtp;
    Button btnOTP,btnLogin;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    String verificationCodebySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        PhoneNumber = findViewById(R.id.PhoneNumber);
        etOtp = findViewById(R.id.etOtp);
        btnLogin = findViewById(R.id.btnLogin);
        btnOTP = findViewById(R.id.btnOTP);

        etOtp.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        progressBar.setVisibility(View.GONE);


        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PhoneNumber.getText().toString().isEmpty())
                {
                    PhoneNumber.setError("Enter Phone Number");
                    PhoneNumber.requestFocus();
                }
                else if (PhoneNumber.getText().toString().length()!=10)
                {
                    PhoneNumber.setError("Invalid Phone Number");
                    PhoneNumber.requestFocus();
                }
                else
                {
                    sendOTP(PhoneNumber.getText().toString().trim());
                    etOtp.setVisibility(View.VISIBLE);
                    btnOTP.setText("Login");
                }
            }
        });
    }



    private void sendOTP(String phno){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodebySystem =s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null)
            {
                etOtp.setText(code);
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this,"Failing "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String verificationCode)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodebySystem,verificationCode);
        signInByCredential(credential);
    }

    private void signInByCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}