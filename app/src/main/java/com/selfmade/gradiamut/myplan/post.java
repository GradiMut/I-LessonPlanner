package com.selfmade.gradiamut.myplan;

import android.content.Intent;
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

public class post extends AppCompatActivity {
    private EditText edt;
    private Button btn;
    private DatabaseReference db;
    private DatabaseReference mRootUser;
    private FirebaseUser mCurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mRootUser = FirebaseDatabase.getInstance().getReference().child("user");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("review").push();
        final String mPostKey = getIntent().getStringExtra("itemId");
        final String mSchoolId = getIntent().getStringExtra("schoolId");
        final String mTopicId = getIntent().getStringExtra("topicId");


        edt = findViewById(R.id.txt);

        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRootUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        db = FirebaseDatabase.getInstance().getReference("review").child(mTopicId);
                        String push = db.push().getKey();

                        String txt = edt.getText().toString();
                        if (!TextUtils.isEmpty(txt)) {
                            db.child("review_content").setValue(txt);
                            db.child("userId").setValue(mCurrentUser.getUid());
                            db.child("postFK").setValue(mPostKey);
                            Toast.makeText(getApplicationContext(), "Comment Send", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(getApplicationContext(), "Empty set", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
