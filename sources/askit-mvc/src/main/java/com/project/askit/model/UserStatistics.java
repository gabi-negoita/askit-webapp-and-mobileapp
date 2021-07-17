package com.project.askit.model;

public class UserStatistics {

    private Integer postedAnswers;
    private Integer postedQuestions;
    private Integer rankingPoints;

    public UserStatistics() {
    }

    public Integer getPostedAnswers() {
        return postedAnswers;
    }

    public void setPostedAnswers(Integer postedAnswers) {
        this.postedAnswers = postedAnswers;
    }

    public Integer getPostedQuestions() {
        return postedQuestions;
    }

    public void setPostedQuestions(Integer postedQuestions) {
        this.postedQuestions = postedQuestions;
    }

    public Integer getRankingPoints() {
        return rankingPoints;
    }

    public void setRankingPoints(Integer rankingPoints) {
        this.rankingPoints = rankingPoints;
    }

    @Override
    public String toString() {
        return "UserStatistics{" +
                "postedAnswers=" + postedAnswers +
                ", postedQuestions=" + postedQuestions +
                ", rankingPoints=" + rankingPoints +
                '}';
    }
}