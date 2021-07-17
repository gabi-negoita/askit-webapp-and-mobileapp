package com.project.askit.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostAnswerModel {

    @NotNull(message = "field is required")
    @Size(min = 30, message = "must be at least 30 characters long")
    @Size(max = 16777215, message = "must be at most 16777215 characters long")
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "PostAnswerModel{" +
                "body='" + body + '\'' +
                '}';
    }
}
