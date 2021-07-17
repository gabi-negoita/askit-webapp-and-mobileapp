package com.project.askit.exeption;

import com.project.askit.model.MessageModel;
import com.project.askit.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ExpiredLinkExeption extends Exception {

    public ExpiredLinkExeption() {
        super();
    }

    public MessageModel getMessageModel(){
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage("Expired link");
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        details.add(new Pair<>(null, "The link you accessed is no longer available"));

        messageModel.setDetails(details);

        return messageModel;
    }


}
