package com.project.askit.entity;

import javax.persistence.*;

@Entity
@Table(name = "question_attachment")
public class QuestionAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "location")
    private String location;

    @Column(name = "question_id")
    private Integer questionId;

    public QuestionAttachment() {
    }

    public QuestionAttachment(String location,
                              Integer questionId) {
        this.location = location;
        this.questionId = questionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
