package com.project.askit.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class ChangeAccountEmailModel {

    private String currentEmail;

    @NotNull(message = "field is required")
    @Email(message = "field is invalid")
    private String newEmail;

    public String getCurrentEmail() {
        return currentEmail;
    }

    public void setCurrentEmail(String currentEmail) {
        this.currentEmail = currentEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    @Override
    public String toString() {
        return "ChangeEmailModel{" +
                "currentEmail='" + currentEmail + '\'' +
                ", newEmail='" + newEmail + '\'' +
                '}';
    }
}
