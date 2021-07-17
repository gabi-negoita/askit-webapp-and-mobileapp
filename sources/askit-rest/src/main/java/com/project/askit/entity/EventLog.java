package com.project.askit.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "event_log")
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "action")
    private String action;

    @Column(name = "info")
    private String info;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "user_id")
    private Integer userId;

    public EventLog() {
    }

    public EventLog(String action,
                    String info,
                    Timestamp createdDate,
                    Integer userId) {
        this.action = action;
        this.info = info;
        this.createdDate = createdDate;
        this.userId = userId;
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
