package com.project.askit.model;

public class EmailModel {
    public static final String CONFIRM_EMAIL = "confirm-email";
    public static final String RESET_PASSWORD_EMAIL = "reset-password";
    public static final String CHANGE_EMAIL = "change-email";
    public static final String INFORM_EMAIL = "inform";

    private String destination;

    private String link;

    private String type;

    public EmailModel() {
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

    public String getConfirmEmailContent() {
        String text = "<p>Hi,</p>";
        text = text + "<p>Thank you for creating an account on Askit!</p>";
        text = text + "<p>To verify your email address, simply click <strong><a href=\"" + this.link + "\">here</a></strong>.</p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p><i>Note that the link will expire in 24h from the time this email was sent.</i></p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p>If you did not create an account using this address, please ignore this email.</p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p>Regards,</p>";
        text = text + "<p>The Askit Team</p>";

        return text;
    }

    public String getResetPasswordEmailContent() {
        String text = "<p>Hi,</p>";
        text = text + "<p>You are receiving this mail because you requested to reset your password on your Askit account.</p>";
        text = text + "<p>To reset your password use this <strong><a href=\"" + this.link + "\">link</a></strong>.</p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p><i>Note that the link will expire in 24h from the time this email was sent.</i></p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p>If you did not request a password reset, please ignore this email.</p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p>Regards,</p>";
        text = text + "<p>The Askit Team</p>";

        return text;
    }

    public String getChangeEmailContent() {
        String text = "<p>Hi,</p>";
        text = text + "<p>You are receiving this mail because you requested an email change on your Askit account.</p>";
        text = text + "<p>To complete your request, simply click <strong><a href=\"" + this.link + "\">here</a></strong>.</p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p>If you did not request an email change, please ignore this email.</p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p>Regards,</p>";
        text = text + "<p>The Askit Team</p>";

        return text;
    }

    public String getInformEmailContent() {
        String text = "<p>Hi,</p>";
        text = text + "<p>You are receiving this mail because someone tried to use this email to access their Askit account.</p>";
        text = text + "<p>If you requested this please be informed that you do not have an Askit account.</p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p>If that person is not you, please ignore this email.</p>";
        text = text + "<p>&nbsp;</p>";
        text = text + "<p>Regards,</p>";
        text = text + "<p>The Askit Team</p>";

        return text;
    }
}