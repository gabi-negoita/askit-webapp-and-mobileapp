package com.project.askit.exeption;

import com.project.askit.model.MessageModel;
import com.project.askit.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class AlreadyActiveAccountException extends Exception {

    public AlreadyActiveAccountException() {
        super();
    }

    public MessageModel getMessageModel(){
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage("Already activated account");
        messageModel.setType(MessageModel.TYPE_WARNING);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "Your account has already been activated"));
        details.add(new Pair<>(null, "No further action are required"));

        messageModel.setDetails(details);

        return messageModel;
    }


}
