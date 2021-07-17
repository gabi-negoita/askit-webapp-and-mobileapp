package com.project.askit.model;

import java.util.Arrays;

public class ChangeAccountRolesModel {

    private int[] roles;

    public ChangeAccountRolesModel() {
    }

    public int[] getRoles() {
        return roles;
    }

    public void setRoles(int[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "ChangeAccountRolesModel{" +
                "roles=" + Arrays.toString(roles) +
                '}';
    }
}
