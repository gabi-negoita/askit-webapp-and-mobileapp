package com.project.askit.model;

import java.sql.Date;

public class UserActivity {

    private Date date;
    private Integer questions;
    private Integer answers;

    public UserActivity() {
    }

    public UserActivity(Date date,
                        Integer questions,
                        Integer answers) {
        this.date = date;
        this.questions = questions;
        this.answers = answers;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getQuestions() {
        return questions;
    }

    public void setQuestions(Integer questions) {
        this.questions = questions;
    }

    public Integer getAnswers() {
        return answers;
    }

    public void setAnswers(Integer answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "UserStatistics{" +
                "date=" + date +
                ", questions=" + questions +
                ", answers=" + answers +
                '}';
    }
}