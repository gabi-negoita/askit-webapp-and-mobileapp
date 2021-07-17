package com.project.askit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.askit.model.AnswerStatistics;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer {

    private Integer id;
    private String htmlText;
    private Timestamp createdDate;
    private Integer questionId;
    private Integer userId;
    private Integer approved;
    private String comment;


    private AnswerStatistics answerStatistics;

    public Answer() {
    }

    public Answer(String htmlText, Timestamp createdDate, Integer questionId, Integer userId, Integer approved) {
        this.htmlText = htmlText;
        this.createdDate = createdDate;
        this.questionId = questionId;
        this.userId = userId;
        this.approved = approved;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
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

    public AnswerStatistics getAnswerStatistics() {
        return answerStatistics;
    }

    public void setAnswerStatistics(AnswerStatistics answerStatistics) {
        this.answerStatistics = answerStatistics;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", htmlText='" + htmlText + '\'' +
                ", createdDate=" + createdDate +
                ", questionId=" + questionId +
                ", userId=" + userId +
                ", approved=" + approved +
                ", comment='" + comment + '\'' +
                ", answerStatistics=" + answerStatistics +
                '}';
    }
}
