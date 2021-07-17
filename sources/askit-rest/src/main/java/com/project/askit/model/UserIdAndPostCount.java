package com.project.askit.model;

public class UserIdAndPostCount {
    int userId;
    int posts;

    public UserIdAndPostCount(int userId,
                              int posts) {
        this.userId = userId;
        this.posts = posts;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }
}