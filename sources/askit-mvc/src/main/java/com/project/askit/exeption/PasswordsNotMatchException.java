package com.project.askit.exeption;

import com.project.askit.model.MessageModel;
import com.project.askit.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PasswordsNotMatchException extends Exception {

    public PasswordsNotMatchException() {
        super();
    }

    public MessageModel getMessageModel(){
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage("Invalid values");
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "The passwords you provided do not match"));

        messageModel.setDetails(details);

        return messageModel;
    }


}
