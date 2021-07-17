package com.project.askit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.askit.model.QuestionStatistics;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {

    private Integer id;
    private String subject;
    private String htmlText;
    private Timestamp createdDate;
    private Integer categoryId;
    private Integer userId;
    private Integer approved;
    private String comment;

    private QuestionStatistics questionStatistics;

    public Question() {
    }

    public Question(String subject, String htmlText, Timestamp createdDate, Integer categoryId, Integer userId, Integer approved) {
        this.subject = subject;
        this.htmlText = htmlText;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
        this.userId = userId;
        this.approved = approved;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public QuestionStatistics getQuestionStatistics() {
        return questionStatistics;
    }

    public void setQuestionStatistics(QuestionStatistics questionStatistics) {
        this.questionStatistics = questionStatistics;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", htmlText='" + htmlText + '\'' +
                ", createdDate=" + createdDate +
                ", categoryId=" + categoryId +
                ", userId=" + userId +
                ", approved=" + approved +
                ", comment='" + comment + '\'' +
                ", questionStatistics=" + questionStatistics +
                '}';
    }
}
