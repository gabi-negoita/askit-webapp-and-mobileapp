package com.project.askit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerStatistics {

    private Integer votes;

    public AnswerStatistics() {
    }

    public AnswerStatistics(Integer votes) {
        this.votes = votes;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "AnswerStatistics{" +
                "votes=" + votes +
                '}';
    }
}
