package com.project.askit.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostQuestionModel {

    @NotNull(message = "field is required")
    @Size(min = 15, message = "must be at least 15 characters long")
    @Size(max = 256, message = "must be at most 256 characters long")
    private String title;

    @NotNull(message = "field is required")
    @NotEmpty(message = "is not selected")
    private String category;

    @NotNull(message = "field is required")
    @Size(min = 30, message = "must be at least 30 characters long")
    @Size(max = 16777215, message = "must be at most 16777215 characters long")
    private String body;

    public String getSubject() {
        return title;
    }

    public void setSubject(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "PostQuestion{" + "title='" + title + '\'' + ", category='" + category + '\'' + ", body='" + body + '\'' + '}';
    }
}
