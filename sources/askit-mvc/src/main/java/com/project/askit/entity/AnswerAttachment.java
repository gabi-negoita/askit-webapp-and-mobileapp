package com.project.askit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerAttachment {

    private Integer id;
    private String location;
    private Integer answerId;

    public AnswerAttachment() {
    }

    public AnswerAttachment(String location,
                            Integer answerId) {
        this.location = location;
        this.answerId = answerId;
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

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    @Override
    public String toString() {
        return "AnswerAttachment{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", answerId=" + answerId +
                '}';
    }
}
