package com.selfmade.gradiamut.myplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleBlog extends AppCompatActivity {

    private DatabaseReference mRoot, mUserRoot;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mCurrentUser;

    private TextView mUserName, mTitle, mDateCreated, mContent;
    private ImageView mProfile;
    private Button mCreateComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_blog);

        final String mPostKey = getIntent().getStringExtra("itemId");
        mDatabase = FirebaseDatabase.getInstance();
        mRoot = mDatabase.getReference().child("blog").child(mPostKey);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserRoot = mDatabase.getReference("user");

        mCreateComment = findViewById(R.id.createComment);

        mCreateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Popup();




            }
        });


    }

    private void Popup() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.comment_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText commentPop = promptsView.findViewById(R.id.blogComment);



        alertDialogBuilder
                  .setCancelable(true)
                  .setPositiveButton("Comment", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          mUserRoot.addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(DataSnapshot dataSnapshot) {
                                  final String commentContent = commentPop.getText().toString();
                                  final String uid = mCurrentUser.getUid();
                                  final String mPostKey = getIntent().getStringExtra("itemId");
                                  String userName = (String) dataSnapshot.child(uid).child("name").getValue();

                                  Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();


                                  DatabaseReference comment = mDatabase.getReference().child("blogComment").child(mPostKey).push();

                                  if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(commentContent)) {
                                      comment.child("name").setValue(userName);
                                      comment.child("commentContent").setValue(commentContent);

                                      Toast.makeText(getApplicationContext(), "Comment sent", Toast.LENGTH_SHORT).show();
                                  } else {
                                      Toast.makeText(getApplicationContext(), "Please fill out the empty filed", Toast.LENGTH_SHORT).show();
                                  }
                              }

                              @Override
                              public void onCancelled(DatabaseError databaseError) {

                              }
                          });

                      }
                  }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Context context;

                mUserName = findViewById(R.id.nameOfUser);
                mTitle = findViewById(R.id.blogTitle);
                mDateCreated = findViewById(R.id.dateCreated);
                mContent = findViewById(R.id.blogContent);
                mProfile = findViewById(R.id.userPicture);

                String username = (String) dataSnapshot.child("created_by").getValue();
                String title = (String) dataSnapshot.child("title").getValue();
                String dateCreated = (String) dataSnapshot.child("created_date").getValue();
                String content = (String) dataSnapshot.child("content").getValue();
                String profilePic = (String) dataSnapshot.child("img").getValue();

                mUserName.setText(username);
                mTitle.setText(title);
                mDateCreated.setText(dateCreated);
                mContent.setText(content);
                //mProfile.setText(profilePic);
                Picasso.with(getApplicationContext()).load(profilePic).into(mProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
