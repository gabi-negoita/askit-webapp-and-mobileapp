package com.project.askit.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "html_text")
    private String htmlText;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "approved")
    private Integer approved;

    @Column(name = "comment")
    private String comment;

    public Question() {
    }

    public Question(String subject,
                    String htmlText,
                    Timestamp createdDate,
                    Integer categoryId,
                    Integer userId,
                    Integer approved,
                    String comment) {
        this.subject = subject;
        this.htmlText = htmlText;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
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
                '}';
    }
}
