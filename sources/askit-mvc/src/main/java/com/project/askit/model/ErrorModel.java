package com.project.askit.model;

import java.util.List;

public class ErrorModel {

    private int code;
    private List<String> details;

    public ErrorModel() {
    }

    public ErrorModel(int code,
                      List<String> details) {
        this.code = code;
        this.details = details;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ErrorModel{" +
                "code=" + code +
                ", details=" + details +
                '}';
    }
}
