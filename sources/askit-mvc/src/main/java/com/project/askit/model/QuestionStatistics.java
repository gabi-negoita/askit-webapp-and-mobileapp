package com.project.askit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionStatistics {

    private Integer votes;

    private Integer answers;

    public QuestionStatistics() {
    }

    public QuestionStatistics(Integer votes, Integer answers) {
        this.votes = votes;
        this.answers = answers;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getAnswers() {
        return answers;
    }

    public void setAnswers(Integer answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuestionStatistics{" +
                "votes=" + votes +
                ", answers=" + answers +
                '}';
    }
}
