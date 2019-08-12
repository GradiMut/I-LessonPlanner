package com.selfmade.gradiamut.myplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selfmade.gradiamut.myplan.Util.Model;
import com.selfmade.gradiamut.myplan.Util.ViewHolder;

public class CreatePlan extends AppCompatActivity {

    public String user_School_id, user_title;
    private DatabaseReference mRoot;
    private DatabaseReference mRootUser;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mCurrentUser;
    private RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        mDatabase = FirebaseDatabase.getInstance();
        mRootUser = mDatabase.getReference().child("user");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        //mRoot = mDatabase.getReference().child("plan").child("");


        mRecycleView = findViewById(R.id.GridRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecycleView.setLayoutManager(layoutManager);

        //
        FloatingActionButton mNewCourse = findViewById(R.id.new_course);

        mNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnAlertBuilder();

            }
        });
    }

    private void OnAlertBuilder() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.subject_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText mSubjectName = promptsView.findViewById(R.id.subject);
        final EditText mYeargroup = promptsView.findViewById(R.id.year_group);
        final EditText mDays = promptsView.findViewById(R.id.subDate);
        final EditText mStdNum = promptsView.findViewById(R.id.stdNum);

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("send",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                mRootUser.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String uid = mCurrentUser.getUid();

                                        user_School_id = (String) dataSnapshot.child(uid).child("school_id").getValue();
                                        user_title = (String) dataSnapshot.child(uid).child("title").getValue();
                                        String user_name = (String) dataSnapshot.child(uid).child("name").getValue();

                                        Toast.makeText(getApplicationContext(), user_School_id, Toast.LENGTH_SHORT).show();
                                        final String subjectName = mSubjectName.getText().toString();
                                        final String yearGroup = mYeargroup.getText().toString();
                                        final String days = mDays.getText().toString();
                                        final String stdNum = mStdNum.getText().toString();


                                        Log.d("dbg", subjectName);
                                        DatabaseReference newSubject =
                                                mDatabase.getReference("plan").child(user_School_id).child("subject").push();


                                        if (!TextUtils.isEmpty(subjectName) && !TextUtils.isEmpty(yearGroup) &&
                                                !TextUtils.isEmpty(days) && !TextUtils.isEmpty(stdNum)) {
                                            newSubject.child("subject_name").setValue(subjectName);
                                            newSubject.child("year_group").setValue(yearGroup);
                                            newSubject.child("days").setValue(days);
                                            newSubject.child("std_num").setValue(stdNum);
                                            newSubject.child("userId").setValue(uid);
                                            newSubject.child("author").setValue(user_name);

                                            Log.d("subjectName", newSubject.toString());
                                            Toast.makeText(getApplicationContext(), "New Subject Created Successfully", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Empty set", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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

        mRootUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = mCurrentUser.getUid();

                user_School_id = (String) dataSnapshot.child(uid).child("school_id").getValue();
                user_title = (String) dataSnapshot.child(uid).child("title").getValue();

                mRoot = mDatabase.getReference().child("plan").child(user_School_id).child("subject");


                FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.grid_plan_row,
                        ViewHolder.class,
                        mRoot
                ) {
                    @Override
                    protected void populateViewHolder(final ViewHolder viewHolder, final Model model, int position) {

                        final String _id = getRef(position).getKey();

                        //TextView subject = findViewById(R.id.grid_subject);

                        viewHolder.setsubject_name(model.getSubject_name());

                        Log.w("dbFire", mRoot.toString());

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(CreatePlan.this, CreateTopic.class);
                                i.putExtra("itemId", _id);
                                i.putExtra("schoolId", user_School_id);
                                startActivity(i);
                                Log.d("idPost", _id);
                            }

                        });


                    }
                };
                mRecycleView.setAdapter(firebaseRecyclerAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("CreatePlanAdd", databaseError.toString());
            }
        });

    }
}
