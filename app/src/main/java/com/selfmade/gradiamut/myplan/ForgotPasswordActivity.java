package com.selfmade.gradiamut.myplan;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @ForgotpasswordActivity handle the process
 * for generating forgot password with Firebase
 * */
public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmail;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEmail = findViewById(R.id.forgotEmailField);
        Button mRestPassword = findViewById(R.id.btnForgotPass);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mRestPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),
                            "Enter your registration email", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("verifying..");
                progressDialog.show();

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            "Reset password instructions has been sent to your email",Toast.LENGTH_SHORT)
                                            .show();
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            "Email don't exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    // if any error occur
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}