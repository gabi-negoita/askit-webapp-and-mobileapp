package com.project.askit.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class ResetPasswordRequestModel {

    @NotNull(message = "field is required")
    @Email(message = "is invalid")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ResetPasswordModel{" +
                "email='" + email + '\'' +
                '}';
    }
}
