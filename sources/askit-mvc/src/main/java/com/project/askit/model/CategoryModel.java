package com.project.askit.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryModel {

    private Integer id;

    @NotNull(message = "field is required")
    @Size(min = 2, message = "must be at least 2 characters long")
    @Size(max = 32, message = "must be at most 32 characters long")
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
