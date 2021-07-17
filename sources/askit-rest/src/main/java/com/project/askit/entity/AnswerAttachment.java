package com.project.askit.entity;

import javax.persistence.*;

@Entity
@Table(name = "answer_attachment")
public class AnswerAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "location")
    private String location;

    @Column(name = "answer_id")
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

    public void setAnswerId(Integer questionId) {
        this.answerId = questionId;
    }
}
