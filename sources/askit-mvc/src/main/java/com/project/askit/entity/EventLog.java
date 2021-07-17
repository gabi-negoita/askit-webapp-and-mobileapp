package com.project.askit.entity;

import java.sql.Timestamp;

public class EventLog {

    private Integer id;
    private String action;
    private String info;
    private Timestamp createdDate;
    private Integer userId;

    public static final String SELECT = "select";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String INSERT = "insert";

    public EventLog() {
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "EventLog{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", info='" + info + '\'' +
                ", createdDate=" + createdDate +
                ", userId=" + userId +
                '}';
    }
}
