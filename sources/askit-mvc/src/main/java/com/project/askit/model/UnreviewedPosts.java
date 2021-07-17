package com.project.askit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnreviewedPosts {

    private int count;

    public UnreviewedPosts() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "UnreviewedQuestions{" +
                "count=" + count +
                '}';
    }
}
