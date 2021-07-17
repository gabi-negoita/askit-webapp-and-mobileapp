package com.project.askit.model;

import com.project.askit.util.Pair;

import java.util.List;

public class MessageModel {

    public static final Pair<String, String> TYPE_ERROR = new Pair<>("error", "red");
    public static final Pair<String, String> TYPE_WARNING = new Pair<>("warning", "yellow");
    public static final Pair<String, String> TYPE_SUCCESS = new Pair<>("success", "green");
    public static final Pair<String, String> TYPE_INFO = new Pair<>("info", "blue");

    private String message;
    private Pair<String, String> type;
    private List<Pair<String, String>> details;

    public MessageModel() {
    }

    public MessageModel(String message,
                        Pair<String, String> type,
                        List<Pair<String, String>> details) {
        this.message = message;
        this.type = type;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Pair<String, String> getType() {
        return type;
    }

    public void setType(Pair<String, String> type) {
        this.type = type;
    }

    public List<Pair<String, String>> getDetails() {
        return details;
    }

    public void setDetails(List<Pair<String, String>> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "message='" + message + '\'' +
                ", type=" + type +
                ", details=" + details +
                '}';
    }
}
