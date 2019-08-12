package com.selfmade.gradiamut.myplan.Util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Model {
    //Blog variable
    public String  created_by, title, content, created_date, img, commentNum;
    //Subject plan variable
    public String subject_name, year_group, author, days, std_num, subject_img;
    //Topic plan variable
    public String topic_name, lesson_focus, lesson_summary, learning_resource;
    //Topic Review plan variable
    public String roll_call, lesson_recap, lesson_topicIntro, activity;
    //COmment
    public String name, commentContent;



    public Model() {
    }

    public Model(String name, String commentContent,String commentNum, String created_by, String title, String content, String created_date, String img, String subject_name, String year_group, String author, String days, String std_num, String subject_img, String topic_name, String lesson_focus, String lesson_summary, String learning_resource, String roll_call, String lesson_recap, String lesson_topicIntro, String activity) {
        this.created_by = created_by;
        this.title = title;
        this.content = content;
        this.created_date = created_date;
        this.img = img;
        this.subject_name = subject_name;
        this.year_group = year_group;
        this.author = author;
        this.days = days;
        this.std_num = std_num;
        this.subject_img = subject_img;
        this.topic_name = topic_name;
        this.lesson_focus = lesson_focus;
        this.lesson_summary = lesson_summary;
        this.learning_resource = learning_resource;
        this.roll_call = roll_call;
        this.lesson_recap = lesson_recap;
        this.lesson_topicIntro = lesson_topicIntro;
        this.activity = activity;
        this.commentNum = commentNum;
        this.commentContent = commentContent;
        this.name = name;
    }

    //Comment

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }


    // Blog Model

    public String getCommentNum() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("comment");


        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    // Lesson Subject model
    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getYear_group() {
        return year_group;
    }

    public void setYear_group(String year_group) {
        this.year_group = year_group;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStd_num() {
        return std_num;
    }

    public void setStd_num(String std_num) {
        this.std_num = std_num;
    }

    public String getSubject_img() {
        return subject_img;
    }

    public void setSubject_img(String subject_img) {
        this.subject_img = subject_img;
    }

    // Lesson Topic model

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getLesson_focus() {
        return lesson_focus;
    }

    public void setLesson_focus(String lesson_focus) {
        this.lesson_focus = lesson_focus;
    }

    public String getLesson_summary() {
        return lesson_summary;
    }

    public void setLesson_summary(String lesson_summary) {
        this.lesson_summary = lesson_summary;
    }

    public String getLearning_resource() {
        return learning_resource;
    }

    public void setLearning_resource(String learning_resource) {
        this.learning_resource = learning_resource;
    }

    // Lesson Topic BreackDown Activity model


    public String getRoll_call() {
        return roll_call;
    }

    public void setRoll_call(String roll_call) {
        this.roll_call = roll_call;
    }

    public String getLesson_recap() {
        return lesson_recap;
    }

    public void setLesson_recap(String lesson_recap) {
        this.lesson_recap = lesson_recap;
    }

    public String getLesson_topicIntro() {
        return lesson_topicIntro;
    }

    public void setLesson_topicIntro(String lesson_topicIntro) {
        this.lesson_topicIntro = lesson_topicIntro;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}


