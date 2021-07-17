package com.project.askit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryStatistics {

    private Integer answeredQuestions;

    private Integer unansweredQuestions;

    public CategoryStatistics() {
    }

    public CategoryStatistics(Integer answeredQuestions,
                              Integer unansweredQuestions) {
        this.answeredQuestions = answeredQuestions;
        this.unansweredQuestions = unansweredQuestions;
    }

    public Integer getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(Integer answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public Integer getUnansweredQuestions() {
        return unansweredQuestions;
    }

    public void setUnansweredQuestions(Integer unansweredQuestions) {
        this.unansweredQuestions = unansweredQuestions;
    }

    @Override
    public String toString() {
        return "CategoryStatistics{" + "answeredQuestions=" + answeredQuestions + ", unansweredQuestions=" + unansweredQuestions + '}';
    }
}
