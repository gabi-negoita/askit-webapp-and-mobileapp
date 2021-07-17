package com.project.askit.model;

public class CategoryIdAndPosts {
    int categoryId;
    int posts;

    public CategoryIdAndPosts() {
    }

    public CategoryIdAndPosts(int categoryId,
                              int posts) {
        this.categoryId = categoryId;
        this.posts = posts;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }
}