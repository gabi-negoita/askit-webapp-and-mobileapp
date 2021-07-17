package com.project.askit.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SendNotificationModel {


    private Integer userId;

    @NotNull(message = "field is required")
    @Size(min = 8, message = "must be at least 8 characters long")
    @Size(max = 64, message = "must be at most 64 characters long")
    private String title;

    @NotNull(message = "field is required")
    @Size(min = 16, message = "must be at least 16 characters long")
    @Size(max = 65535, message = "must be at most 65535 characters long")
    private String content;

    private boolean broadcast;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast != null;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SendNotificationModel{" +
                "userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", broadcast=" + broadcast +
                '}';
    }
}
