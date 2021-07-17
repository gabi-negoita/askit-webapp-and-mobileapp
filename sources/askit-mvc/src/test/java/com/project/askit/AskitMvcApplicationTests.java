package com.project.askit;

import com.project.askit.rest.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.text.DateFormat;

@SpringBootTest
class AskitMvcApplicationTests {

    @Autowired
    EmailRestApi emailRestApi;

    @Autowired
    UserRestApi userRestApi;

    @Autowired
    CategoryRestApi categoryRestApi;

    @Autowired
    FileUploadRestApi fileUploadRestApi;

    @Autowired
    QuestionVoteRestApi questionVoteRestApi;

    @Autowired
    AnswerRestApi answerRestApi;

    @Autowired
    QuestionRestApi questionRestApi;

    @Autowired
    RoleRestApi roleRestApi;

    @Autowired
    EventLogRestApi eventLogRestApi;

    @Test
    void contextLoads() {

//        String text = "abc def";
//        System.out.println("Text: " + text);
//
//        String encText = Crypto.encrypt(text, Crypto.KEY);
//        System.out.println("Encr: " + encText);
//        System.out.println("Decr: " + Crypto.decrypt(encText, Crypto.KEY));

//        User user = userRestApi.findById(1);
//
//        EmailModel emailModel = new EmailModel();
//        emailModel.setDestination("negoita.gabi@yahoo.com");
//        emailModel.setType(EmailModel.TYPE_CONFIRM_EMAIL);
//        emailModel.setLink(emailModel.generateLink(user));
//
//        emailRestApi.sendEmail(emailModel);

//        List<User> user = userRestApi.findAll(null,null,null,null,null,"jlawhflajfh@email.com",null).getContent();
//        System.out.println(user);

//    System.out.println(utilRestApi.getAbsolutePath());

//        String text = "abc def ghi";
//        String encText = Crypto.encrypt(text, Crypto.KEY);
//        String decText = Crypto.decrypt(encText, Crypto.KEY);
//
//        System.out.println(text);
//        System.out.println(encText);
//        System.out.println(decText);

//        User user = new User("gabi", "{noop}abcdef123", "bcdef12345@email.com", null, null, new Timestamp(System.currentTimeMillis()), 0);
//        User saved = userRestApi.save(user);
//
//        System.out.println(saved);

//        questionVoteRestApi.deleteById(16);

//        System.out.println(answerRestApi.findAll(null,null,"votes","desc",1,1,null));

//        System.out.println(questionRestApi.findAll(null, null, null, null, null, null, null, null, 0));

//        System.out.println(userRestApi.getActivity(1));

//        System.out.println(answerRestApi.findAll(null, null,null,null,null,null,null).getContent().size());
//        System.out.println(answerRestApi.findAll(null, 10,null,null,null,null,null).getContent().size());
//        System.out.println(answerRestApi.findAll(null, 10,"createdDate","desc",null,null,null).getContent().size());

//        System.out.println(userRestApi.findAll(null,null,null,null,null,null));

//        System.out.println(roleRestApi.findAll(null,null,null,null,null));
//        System.out.println(roleRestApi.findById(53122));

//        System.out.println(eventLogRestApi.findAll(null,null,null,null,null,null,null));
//        EventLog eventLog = new EventLog("delete", "info", new Timestamp(System.currentTimeMillis()), 2);
//        eventLogRestApi.save(eventLog);

//        System.out.println(userRestApi.findAllByUsernameOrEmail(null,5,"username","asc","mike","mike"));

//        Date date = Date.valueOf("2021-02-10");
//        System.out.println(date);
//        System.out.println(userRestApi.getMostActiveUsers(date, 5));

//        System.out.println(questionRestApi.getUnreviewedQuestionsCountByDays(30));
    }
}