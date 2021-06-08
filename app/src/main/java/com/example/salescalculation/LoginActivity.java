package com.example.salescalculation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText PhoneNumber;
    Button btnOTP;
    CountryCodePicker ccp;
    FirebaseAuth auth;
    String number;
    EditText etOTP;
    Button btnVerify;
    String otpid;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        PhoneNumber = findViewById(R.id.PhoneNumber);
        ccp = findViewById(R.id.ccp);
        btnOTP = findViewById(R.id.btnOTP);
        ccp.registerCarrierNumberEditText(PhoneNumber);
        auth = FirebaseAuth.getInstance();
        btnVerify=findViewById(R.id.btnVerify);
        etOTP = findViewById(R.id.etOTP);

        if (auth.getCurrentUser()!=null)
        {
            progress = new ProgressDialog(this);
            progress.setTitle("Logging the User");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setProgress(0);
            progress.show();
            startActivity( new Intent(LoginActivity.this,MainActivity.class));
        }

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
                    number = ccp.getFullNumberWithPlus().trim();
                    initiateotp();
                    btnOTP.setVisibility(View.GONE);
                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOTP.getText().toString().isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"Field must not be empty",Toast.LENGTH_LONG).show();
                }
                else if (etOTP.getText().toString().length()!=6)
                {
                    Toast.makeText(LoginActivity.this,"Invalid OTP",Toast.LENGTH_LONG).show();
                }
                else
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid,etOTP.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void initiateotp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+"+number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
                            {
                                otpid=s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                final String code = phoneAuthCredential.getSmsCode();
                                if (code != null) {
                                    etOTP.setText(code);
                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid,code);
                                    signInWithPhoneAuthCredential(phoneAuthCredential);
                                }

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}