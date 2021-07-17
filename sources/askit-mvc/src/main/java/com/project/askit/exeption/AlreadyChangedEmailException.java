package com.project.askit.exeption;

import com.project.askit.model.MessageModel;
import com.project.askit.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class AlreadyChangedEmailException extends Exception {

    public AlreadyChangedEmailException() {
        super();
    }

    public MessageModel getMessageModel(){
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage("Already changed email");
        messageModel.setType(MessageModel.TYPE_WARNING);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "Your email has already been changed"));
        details.add(new Pair<>(null, "No further action are required"));

        messageModel.setDetails(details);

        return messageModel;
    }


}
