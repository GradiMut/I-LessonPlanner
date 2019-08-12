package com.selfmade.gradiamut.myplan.Util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selfmade.gradiamut.myplan.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    public LinearLayout mCommentLayout;

    //Blog row variable

    //Plan row variable

    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mCommentLayout = itemView.findViewById(R.id.commentLayout);
    }


    //Comment
    public void setName(String name) {
        TextView mCommenterName = mView.findViewById(R.id.commenter_name);
        mCommenterName.setText(name);
    }

    public void setCommentContent(String commentContent) {
        TextView mCommentContent = mView.findViewById(R.id.commentContent);
        mCommentContent.setText(commentContent);    }

    //Blog viewHolder


    /*public void setCreated_by(String created_by) {
        TextView mCreatedBy = mView.findViewById(R.id.row_name);
        mCreatedBy.setText(created_by);
    }*/

    public void setTitle(String title) {
        TextView mTitle = mView.findViewById(R.id.row_title);
        mTitle.setText(title);
    }

    public void setContent(String content) {
        TextView mContent = mView.findViewById(R.id.row_description);
        mContent.setText(content);
    }

    public void setCreated_date(String created_date) {
        TextView mCreatedDate = mView.findViewById(R.id.row_created_date);
        mCreatedDate.setText(created_date);
    }

    public void setCommentNum(String commentNum) {
        TextView mCommentNum = mView.findViewById(R.id.commentNumber);
        mCommentNum.setText(commentNum);
    }

    public void setImg(Context context,String img) {
        ImageView mProfileImg = mView.findViewById(R.id.profile_user);
        Picasso.with(context).load(img).into(mProfileImg);
    }

    //Subject plan viewHolder
    public void setSubject_name(String subject_name) {
        TextView mSubject = itemView.findViewById(R.id.subjectTxt);
        mSubject.setText(subject_name);
    }

    public void setsubject_name(String subject_name) {
        TextView mSubject = itemView.findViewById(R.id.grid_subject);
        mSubject.setText(subject_name);
    }

    public void setYear_group(String year_group) {
        TextView mYearGroup = itemView.findViewById(R.id.academic_yearTxt);
        mYearGroup.setText(year_group);
    }

    public void setAuthor(String author) {
        TextView mAuthor = itemView.findViewById(R.id.authorTxt);
        mAuthor.setText(author);
    }

    public void setDays(String days) {
        TextView mDays = itemView.findViewById(R.id.dateTxt);
        mDays.setText(days);
    }

    public void setStd_num(String std_num) {
        TextView mStdNum = itemView.findViewById(R.id.StdNumTxt);
        mStdNum.setText(std_num);
    }

    public void setSubject_img(Context context, String subject_img) {
        ImageView mSubjectImg = itemView.findViewById(R.id.subject_img);
        Picasso.with(context).load(subject_img).into(mSubjectImg);
    }

    //Topic plan viewHolder
    public void setTopic_name(String topic_name) {
        TextView mTopicName = itemView.findViewById(R.id.getName);
        mTopicName.setText(topic_name);
    }

    public void setLesson_focus(String lesson_focus) {
        TextView mLessonFocus = itemView.findViewById(R.id.lessonfocusView);
        mLessonFocus.setText(lesson_focus);
    }

    public void setLesson_summary(String lesson_summary) {
        TextView mLessonSummary = itemView.findViewById(R.id.lessonSummaryView);
        mLessonSummary.setText(lesson_summary);
    }

    public void setLearning_resource(String learning_resource) {
        TextView mLearningResource = itemView.findViewById(R.id.lessonResourceView);
    }

    //Topic plan activity breakDown viewHolder

    public void setRoll_call(String roll_call) {
        TextView mRollCall = itemView.findViewById(R.id.rollCallView);
        mRollCall.setText(roll_call);
    }

    public void setLesson_recap(String lesson_recap) {
        TextView mLessonRecap = itemView.findViewById(R.id.lessonRecapView);
        mLessonRecap.setText(lesson_recap);
    }

    public void setLesson_topicIntro(String lesson_topicIntro) {
        TextView mLessonIntro = itemView.findViewById(R.id.lessonIntro);
        mLessonIntro.setText(lesson_topicIntro);
    }

    public void setActivity(String activity) {
        TextView mActivity = itemView.findViewById(R.id.activityView);
        mActivity.setText(activity);
    }







}
