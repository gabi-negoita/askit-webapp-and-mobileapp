package com.project.askit.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResetPasswordModel {

    private String key;

    @NotNull(message = "field is required")
    @Size(min = 8, message = "must be at least 8 characters long")
    @Size(max = 128, message = "must be at most 128 characters long")
    private String password;

    @NotNull(message = "field is required")
    private String confirmedPassword;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordModel{" +
                "password='" + password + '\'' +
                ", confirmedPassword='" + confirmedPassword + '\'' +
                '}';
    }
}
