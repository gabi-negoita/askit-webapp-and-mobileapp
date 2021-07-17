package com.project.askit.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignupModel {

    @NotNull(message = "field is required")
    @Size(min = 3, message = "must be at least 3 characters long")
    @Size(max = 32, message = "must be at most 32 characters long")
    private String username;

    @NotNull(message = "field is required")
    @Email(message = "is invalid")
    private String email;

    @NotNull(message = "field is required")
    @Size(min = 8, message = "must be at least 8 characters long")
    @Size(max = 128, message = "must be at most 128 characters long")
    private String password;

    @NotNull(message = "field is required")
    private String confirmedPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "Signup{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmedPassword='" + confirmedPassword + '\'' +
                '}';
    }
}
