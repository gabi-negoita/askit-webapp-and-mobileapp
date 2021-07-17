package com.project.askit.entity;

public class QuestionVote {

    private Integer id;

    private Integer vote;

    private Integer questionId;

    private Integer userId;

    public QuestionVote() {
    }

    public QuestionVote(Integer vote,
                        Integer questionId,
                        Integer userId) {
        this.vote = vote;
        this.questionId = questionId;
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

    @Override
    public String toString() {
        return "QuestionVote{" +
                "id=" + id +
                ", vote=" + vote +
                ", questionId=" + questionId +
                ", userId=" + userId +
                '}';
    }
}
