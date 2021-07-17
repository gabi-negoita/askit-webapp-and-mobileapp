package com.project.askit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAttachment {

    private Integer id;
    private String location;
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

    @Override
    public String toString() {
        return "QuestionAttachment{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", questionId=" + questionId +
                '}';
    }
}
