package com.project.askit.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

public class ChangeAccountInfoModel {

    @NotNull(message = "field is required")
    @Size(min = 3, message = "must be at least 3 characters long")
    @Size(max = 32, message = "must be at most 32 characters long")
//    @Pattern(regexp = "^[a-zA-Z]+$", message = "must start with a letter")
    private String username;

    private Date dateOfBirth;

    private String description;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ChangeInfoModel{" +
                "username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", description='" + description + '\'' +
                '}';
    }
}
