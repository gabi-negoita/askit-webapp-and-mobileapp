package com.project.askit.rest;

import com.project.askit.model.EmailModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@RestController
public class EmailRestController {

    private final JavaMailSender emailSender;
    private final Environment environment;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EmailRestController(JavaMailSender emailSender,
                               Environment environment) {
        this.emailSender = emailSender;
        this.environment = environment;
    }

    @PostMapping("/api/send-email")
    public Object sendEmail(@RequestBody EmailModel emailModel) {
        try {
            // Create message
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set source and destination
            helper.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
            helper.setTo(emailModel.getDestination());

            // Set subject and body
            switch (emailModel.getType()) {
                case EmailModel.CONFIRM_EMAIL: {
                    helper.setSubject("Askit email confirmation");
                    helper.setText(emailModel.getConfirmEmailContent(), true);
                    break;
                }
                case EmailModel.RESET_PASSWORD_EMAIL: {
                    helper.setSubject("Askit password reset");
                    helper.setText(emailModel.getResetPasswordEmailContent(), true);
                    break;
                }
                case EmailModel.CHANGE_EMAIL: {
                    helper.setSubject("Askit email change");
                    helper.setText(emailModel.getChangeEmailContent(), true);
                    break;
                }
                case EmailModel.INFORM_EMAIL: {
                    helper.setSubject("Askit informational email");
                    helper.setText(emailModel.getInformEmailContent(), true);
                    break;
                }
            }

            // Send email
            emailSender.send(message);
        } catch (MessagingException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return emailModel;
    }
}