package com.selfmade.gradiamut.myplan;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class CreateTopic extends AppCompatActivity implements VerticalStepperForm {

    private VerticalStepperFormLayout mStepperForm;
    private EditText mTopicName, mLessonFocus, mLessonSummary, mLearningSrc,
                     mRoolCall, mRecap,mLessonTopic, mActivity;
    private DatabaseReference db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);


         db = FirebaseDatabase.getInstance().getReference("plan");


        String[] mySteps = {"Topic set up", "Lesson Activity Break Down"};

        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        mStepperForm = findViewById(R.id.stepper);

        VerticalStepperFormLayout.Builder.newInstance(mStepperForm, mySteps, this,this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .init();



    }

    /**
     * This method will be called automatically by the system to generate the view of
     * the content of each step.
     * You have to implement the generation of the corresponding step view and return it
     * */
    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;

        switch (stepNumber) {
            case 0:
                view = TopicView();

                break;
            case 1:
                view = Review();
                break;
        }
        return view;
    }


    private View TopicView() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout mTopicLayout = (LinearLayout) inflater.inflate(R.layout.topic_layout, null, false);

        mTopicName = mTopicLayout.findViewById(R.id.topic);
        mLessonFocus = mTopicLayout.findViewById(R.id.lessonfocus);
        mLessonSummary = mTopicLayout.findViewById(R.id.lessonSum);
        mLearningSrc = mTopicLayout.findViewById(R.id.lessonR);

        return mTopicLayout;
    }

    private View Review() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout mBreakDownLayout = (LinearLayout) inflater.inflate(R.layout.activity_breakdown, null, false);


        mRoolCall = mBreakDownLayout.findViewById(R.id.rollCall);
        mRecap = mBreakDownLayout.findViewById(R.id.recap);
        mLessonTopic = mBreakDownLayout.findViewById(R.id.lTopic);
        mActivity = mBreakDownLayout.findViewById(R.id.activity);


        return mBreakDownLayout;
    }

//    private View SubjectView() {
//
//        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
//        LinearLayout mSubjectLayout = (LinearLayout) inflater.inflate(R.layout.subject_layout, null, false);
//        mSubjectName = mSubjectLayout.findViewById(R.id.subject);
//        mYeargroup = mSubjectLayout.findViewById(R.id.year_group);
//        mDate = mSubjectLayout.findViewById(R.id.subDate);
//        mStdNum = mSubjectLayout.findViewById(R.id.stdNum);
//
//        return mSubjectLayout;
//    }

    /** This method will be called every time a step is open, so it can be used for checking conditions.
     *  It is noteworthy that the button “Continue” is disabled by default in every step,
     *  so it will only show up after certain user actions (for example, after the introduction of a correct name or email):
     * */
    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case 0:
                mStepperForm.setStepAsCompleted(0);
                break;
            case 1:
                mStepperForm.setStepAsCompleted(1);
                break;
        }


    }

//    private void validateSubjectInput() {
//        String sub = mSubjectName.getText().toString().trim();
//        String subyearGroup = mYeargroup.getText().toString().trim();
//        String subDate = mDate.getText().toString().trim();
//        String subStdNum = mStdNum.getText().toString().trim();
//
////        if (!sub.isEmpty() && !subyearGroup.isEmpty() && !subDate.isEmpty() && !subStdNum.isEmpty()) {
////            mStepperForm.setStepAsCompleted(0);
////        } else {
////            String errorMessage = "Some Field are empty from Subject";
////            mStepperForm.setActiveStepAsUncompleted(errorMessage);
////        }
//
//    }

    private void validateReviewInput() {


    }

    private void validateTopicInput() {

       String tpName = mTopicName.getText().toString();
//
//        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
//        LinearLayout mTopicLayout = (LinearLayout) inflater.inflate(R.layout.topic_layout, null, false);
//
//        mTopicName = mTopicLayout.findViewById(R.id.topic);
//        mLessonFocus = mTopicLayout.findViewById(R.id.lessonfocus);
//        mLessonSummary = mTopicLayout.findViewById(R.id.lessonSum);
//        mLearningSrc = mTopicLayout.findViewById(R.id.lessonR);

        if (tpName.length() >= 5 ) {
            mStepperForm.setActiveStepAsCompleted();
        } else {
            String errorMessage = "Some Field are empty";
            mStepperForm.setActiveStepAsUncompleted(errorMessage);
        }
    }

    /**This method implement the sending of the data.*/
    @Override
    public void sendData() {
        //        Fragment fragment = null;
//        fragment = new PlanFragment();
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                       .replace(R.id.fragment, fragment)
//                       .addToBackStack(null)
//                       .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        String mPostKey = getIntent().getStringExtra("itemId");
        String mSchoolId = getIntent().getStringExtra("schoolId");

        DatabaseReference dbRefernce = db.child(mSchoolId).child("topic").child(mPostKey).push();

        String tpName = mTopicName.getText().toString();
        String lFocus = mLessonFocus.getText().toString();
        String lSummary = mLessonSummary.getText().toString() ;
        String lSrc = mLearningSrc.getText().toString() ;

        String roolCall = mRoolCall.getText().toString() ;
        String recap = mRecap.getText().toString() ;
        String lTopicIntro = mLessonTopic.getText().toString() ;
        String activity = mActivity.getText().toString();

        if (!TextUtils.isEmpty(tpName) && !TextUtils.isEmpty(lFocus) && !TextUtils.isEmpty(lSummary) && !TextUtils.isEmpty(lSrc) &&
                !TextUtils.isEmpty(roolCall) && !TextUtils.isEmpty(recap) && !TextUtils.isEmpty(lTopicIntro) && !TextUtils.isEmpty(activity))
        {
            dbRefernce.child("topic_name").setValue(tpName);
            dbRefernce.child("lesson_focus").setValue(lFocus);
            dbRefernce.child("lesson_summary").setValue(lSummary);
            dbRefernce.child("lesson_resource").setValue(lSrc);
            dbRefernce.child("roll_call").setValue(roolCall);
            dbRefernce.child("lesson_recap").setValue(recap);
            dbRefernce.child("lesson_topicIntro").setValue(lTopicIntro);
            dbRefernce.child("activity").setValue(activity);

            Toast.makeText(getApplicationContext(), "Topic Send", Toast.LENGTH_SHORT).show();


            startActivity(new Intent(CreateTopic.this, MainActivity.class));

        } else {
            Toast.makeText(getApplicationContext(), "Empty field", Toast.LENGTH_SHORT).show();
        }





    }

}
