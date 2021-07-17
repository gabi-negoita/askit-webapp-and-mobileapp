package com.project.askit.model;

public class EmailModel {

    /* Types:
     * confirm-email: Sign up email confirmation
     * reset-password: Reset password request
     * */

    public static final String CONFIRM_EMAIL = "confirm-email";
    public static final String RESET_PASSWORD_EMAIL = "reset-password";
    public static final String CHANGE_EMAIL = "change-email";
    public static final String INFORM_EMAIL = "inform";
    public static final String DELIMITER = "<!>";

    private String destination;

    private String link;

    private String type;

    public EmailModel() {
    }

    public EmailModel(String destination,
                      String link,
                      String type) {
        this.destination = destination;
        this.link = link;
        this.type = type;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "EmailModel{" +
                "destination='" + destination + '\'' +
                ", link='" + link + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
