package com.project.askit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.askit.model.CategoryStatistics;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

    private Integer id;

    private String title;

    private CategoryStatistics statistics;

    public Category() {
    }

    public Category(String title) {
        this.title = title;
    }

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

    public CategoryStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(CategoryStatistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", statistics=" + statistics +
                '}';
    }
}
