package com.project.askit.rest.api;

import com.project.askit.entity.AnswerAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AnswerAttachmentRestApi {

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AnswerAttachmentRestApi(Environment environment) {
        this.environment = environment;
    }

    public AnswerAttachment save(AnswerAttachment answerAttachment) {
        AnswerAttachment result = null;

        try {
            String uri = environment.getProperty("rest.url.api") + "/answer-attachments";
            HttpEntity<AnswerAttachment> requestEntity = new HttpEntity<>(answerAttachment, null);
            ResponseEntity<AnswerAttachment> response = new RestTemplate().exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            HttpStatus statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                String errorMessage = uri + ": " + response.getBody();
                throw new Exception(errorMessage);
            }

            result = response.getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
