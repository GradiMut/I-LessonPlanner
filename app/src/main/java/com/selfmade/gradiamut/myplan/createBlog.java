package com.selfmade.gradiamut.myplan;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class createBlog extends AppCompatActivity {
    private Button mFabSend;
    private EditText mCommentBody, mCommentTitle;
    DatabaseReference userRoot, commentRoot;
    FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        mFabSend = findViewById(R.id.fab_send);
        mCommentBody = findViewById(R.id.commentBody);
        mCommentTitle = findViewById(R.id.commentTitle);

        userRoot = FirebaseDatabase.getInstance().getReference("user");
        commentRoot = FirebaseDatabase.getInstance().getReference("blog");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        userRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String username = (String) dataSnapshot.child(mCurrentUser.getUid()).child("name").getValue();

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();

                final String created_date = formatter.format(date);
                final String content = mCommentBody.getText().toString().trim();
                final String title = mCommentTitle.getText().toString().trim();


                mFabSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(created_date) && !TextUtils.isEmpty(content) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(username) ){
                            DatabaseReference db = commentRoot.push();
                            db.child("created_date").setValue(created_date);
                            db.child("created_by").setValue(username);
                            db.child("content").setValue(content);
                            db.child("title").setValue(title);

                            Toast.makeText(getApplicationContext(), "Blog created", Toast.LENGTH_LONG).show();


                            startActivity(new Intent(createBlog.this, MainActivity.class));
                        }


                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

