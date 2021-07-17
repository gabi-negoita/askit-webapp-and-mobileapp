package com.project.askit.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangeAccountPasswordModel {

    private String username;

    @NotNull(message = "field is required")
    @Size(min = 8, message = "must be at least 8 characters long")
    @Size(max = 128, message = "must be at most 128 characters long")
    private String currentPassword;

    @NotNull(message = "field is required")
    @Size(min = 8, message = "must be at least 8 characters long")
    @Size(max = 128, message = "must be at most 128 characters long")
    private String newPassword;

    @NotNull(message = "field is required")
    @Size(min = 8, message = "must be at least 8 characters long")
    @Size(max = 128, message = "must be at most 128 characters long")
    private String confirmNewPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordModel{" +
                "username='" + username + '\'' +
                ", currentPassword='" + currentPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmNewPassword='" + confirmNewPassword + '\'' +
                '}';
    }
}
