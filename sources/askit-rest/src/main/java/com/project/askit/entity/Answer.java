package com.project.askit.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "html_text")
    private String htmlText;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "approved")
    private Integer approved;

    @Column(name = "comment")
    private String comment;

    public Answer() {
    }

    public Answer(String htmlText,
                  Timestamp createdDate,
                  Integer questionId,
                  Integer userId,
                  Integer approved,
                  String comment) {
        this.htmlText = htmlText;
        this.createdDate = createdDate;
        this.questionId = questionId;
        this.userId = userId;
        this.approved = approved;
        this.comment = comment;
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
                '}';
    }
}