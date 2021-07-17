package com.project.askit.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginModel {

    @NotNull(message = "field is required")
    @Email(message = "is invalid")
    private String email;

    @NotNull(message = "field is required")
    @Size(min = 8, message = "must be at least 8 characters long")
    @Size(max = 128, message = "must be at most 128 characters long")
    private String password;

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

    @Override
    public String toString() {
        return "Login{" + "email='" + email + '\'' + ", password='" + password + '\'' + '}';
    }
}
