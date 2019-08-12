package com.selfmade.gradiamut.myplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.selfmade.gradiamut.myplan.Util.Model;
import com.selfmade.gradiamut.myplan.Util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SinglePlan extends AppCompatActivity {

    private DatabaseReference mRoot;
    private Spinner mSpinner;
    private DatabaseReference mRootUser;

    private Button mReview;
    private TextView mCommentText, mRV;
    private FirebaseUser mCurrentUser;

    public String user_School_id, user_title, spinnerItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_plan);

        final String mPostKey = getIntent().getStringExtra("itemId");
        final String mSchoolId = getIntent().getStringExtra("schoolId");

        mRoot = FirebaseDatabase.getInstance().getReference().child("plan").child(mSchoolId).child("topic").child(mPostKey);
        mRootUser = FirebaseDatabase.getInstance().getReference().child("user");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mReview = findViewById(R.id.review);
        mCommentText = findViewById(R.id.txtComment);
        mRV = findViewById(R.id.rv);


        Log.w("school_id", mSchoolId + mRoot);
        Log.w("post_id", mPostKey);


        // Showing the create button according to the title of the user


    }

    @Override
    protected void onStart() {
        super.onStart();
        final String mpostKey = getIntent().getStringExtra("itemId");
        final String mschoolId = getIntent().getStringExtra("schoolId");
        final String muserTitle = getIntent().getStringExtra("userTitle");


        mRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String mPostKey = getIntent().getStringExtra("itemId");


                mSpinner = findViewById(R.id.spinner);
                final List<String> topic_name_list = new ArrayList<String>();

                for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                    String topic_name = (String) topicSnapshot.child("topic_name").getValue();

                    topic_name_list.add(topic_name);

                }

                String tp_id = (String) dataSnapshot.child("topic_name").getKey();
                Log.d("tp_id", "onDataChange: " + tp_id);

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>
                        (SinglePlan.this, android.R.layout.simple_spinner_item, topic_name_list);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpinner.setAdapter(spinnerAdapter);
                Log.w("spinnerAda", spinnerAdapter.toString());

                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnerItem = (String) parent.getItemAtPosition(position);
                        Log.d("SpinnerItem", "onDataChange: " + spinnerItem);

                        Query query = mRoot.orderByChild("topic_name").equalTo(spinnerItem);

                        // Get the id of the selected spinner
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    final String key = child.getKey();
                                    Log.d("StackOverFlow", key);
                                    mReview.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(getApplicationContext(), post.class);
                                            i.putExtra("itemId", mPostKey);
                                            i.putExtra("schoolId", user_School_id);
                                            i.putExtra("topicId", key);
                                            startActivity(i);
                                        }
                                    });

                                    // Get All info according to the item selected in spinner
                                    mRoot.child(key).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String mFocusLesson = (String) dataSnapshot.child("lesson_focus").getValue();
                                            String mSummaryLesson = (String) dataSnapshot.child("lesson_summary").getValue();
                                            String mLessonResource = (String) dataSnapshot.child("lesson_resource").getValue();

                                            String mCallRoll = (String) dataSnapshot.child("roll_call").getValue();
                                            String mRecapLesson = (String) dataSnapshot.child("lesson_recap").getValue();
                                            String mIntroTopic = (String) dataSnapshot.child("lesson_topicIntro").getValue();
                                            String mLessonActivity = (String) dataSnapshot.child("activity").getValue();


                                            TextView mLessonFocus = findViewById(R.id.lessonfocusView);
                                            TextView mLessonSummary = findViewById(R.id.lessonSummaryView);
                                            TextView mLearningResource = findViewById(R.id.lessonResourceView);

                                            TextView mRollCall = findViewById(R.id.rollCallView);
                                            TextView mLessonRecap = findViewById(R.id.lessonRecapView);
                                            TextView mLessonIntro = findViewById(R.id.lessonIntro);
                                            TextView mActivity = findViewById(R.id.activityView);


                                            mLessonFocus.setText(mFocusLesson);
                                            mLessonSummary.setText(mSummaryLesson);
                                            mLearningResource.setText(mLessonResource);

                                            mRollCall.setText(mCallRoll);
                                            mLessonRecap.setText(mRecapLesson);
                                            mLessonIntro.setText(mIntroTopic);
                                            mActivity.setText(mLessonActivity);


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    //Review or comment made by the director
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("review").child(key);


                                    db.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            String dataExist = String.valueOf(dataSnapshot.getChildren().iterator().hasNext());
                                            Log.d("dataExist", dataExist);
                                            String reviewContent = (String) dataSnapshot.child("review_content").getValue();

                                            // Autorization

                                            if (dataSnapshot.getChildren().iterator().hasNext()) {
                                                if (muserTitle.equals("teacher")) {
                                                    mReview.setVisibility(View.GONE);
                                                    mCommentText.setVisibility(View.VISIBLE);
                                                    mRV.setVisibility(View.VISIBLE);

                                                    Toast.makeText(getApplicationContext(), muserTitle, Toast.LENGTH_SHORT).show();


                                                } else if (muserTitle.equals("director")) {
                                                    mReview.setVisibility(View.GONE);
                                                    mCommentText.setVisibility(View.VISIBLE);
                                                    mRV.setVisibility(View.VISIBLE);

                                                    Toast.makeText(getApplicationContext(), muserTitle, Toast.LENGTH_SHORT).show();

                                                }
                                            } else {
                                                if (muserTitle.equals("teacher")) {
                                                    mReview.setVisibility(View.GONE);
                                                    mCommentText.setVisibility(View.GONE);
                                                    mRV.setVisibility(View.GONE);

                                                    Toast.makeText(getApplicationContext(), muserTitle, Toast.LENGTH_SHORT).show();


                                                } else if (muserTitle.equals("director")) {
                                                    mReview.setVisibility(View.VISIBLE);
                                                    mCommentText.setVisibility(View.GONE);
                                                    mRV.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(), muserTitle, Toast.LENGTH_SHORT).show();


                                                }
                                            }

                                            if (reviewContent != null) {
                                                Log.d("review", reviewContent);
                                            } else {
                                                Log.d("review", "No data found");

                                            }

                                            mCommentText.setText(reviewContent);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("FirebaseErrorSinglePlan", databaseError.toString());
            }
        });

    }

}
