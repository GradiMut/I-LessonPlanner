package com.selfmade.gradiamut.myplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseAuth mAth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button mLogin = findViewById(R.id.signin);
        Button mForgotPassword = findViewById(R.id.forgot_pass);
        Button mNewAccount = findViewById(R.id.new_account);

        mEmail = findViewById(R.id.emailField);
        mPassword = findViewById(R.id.passwordField);

        mAth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseUser = mDatabase.getReference().child("user");

        progressDialog = new ProgressDialog(this);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });


        // TODO: Allow user to create a new account by launching the wep page.

        mNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/"));
                startActivity(i);
            }
        });

        // TODO: launch ForgotPasswordActivity
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

    }

    private void Login() {

        String email = mEmail.getText().toString().trim();
        String pass = mPassword.getText().toString().trim();

        // TODO: Validate and Check if user has an account.
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

            progressDialog.setMessage("Logging...");
            progressDialog.show();

            mAth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                checkUserExist();
                                progressDialog.dismiss();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Password or E-mail incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                // If any error occur, get message.
                public void onFailure(@NonNull Exception e) {
                    //progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(),
                                "Please verify your internet connection", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Toast.makeText(getApplicationContext(),
                    "One of the field where empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkUserExist() {
        final String user_id = mAth.getCurrentUser().getUid();

        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    progressDialog.dismiss();
                    Intent mainIntent =
                            new Intent(LoginActivity.this, MainActivity.class);

                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }else{

                    Toast.makeText(getApplicationContext(), "You don't have an account",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LoginFirebase", databaseError.getDetails());

            }
        });

    }
}
