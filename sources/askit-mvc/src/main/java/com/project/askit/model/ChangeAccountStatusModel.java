package com.project.askit.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ChangeAccountStatusModel {

    @NotNull(message = "field is required")
    @Min(value = -1, message = "field value is invalid")
    @Max(value = 1, message = "field value is invalid")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ChangeAccountStatusModel{" +
                "status=" + status +
                '}';
    }
}
