package com.project.askit.entity;

public class AnswerVote {

    private Integer id;
    private Integer vote;
    private Integer answerId;
    private Integer userId;

    public AnswerVote() {
    }

    public AnswerVote(Integer vote,
                      Integer answerId,
                      Integer userId) {
        this.vote = vote;
        this.answerId = answerId;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AnswerVote{" +
                "id=" + id +
                ", vote=" + vote +
                ", answerId=" + answerId +
                ", userId=" + userId +
                '}';
    }
}
