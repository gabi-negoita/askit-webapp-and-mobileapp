package com.project.askit.exeption;

import com.project.askit.model.MessageModel;
import com.project.askit.util.Pair;
import com.project.askit.util.Utility;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class InvalidFieldsException extends Exception {

    private final BindingResult result;

    public InvalidFieldsException(BindingResult result) {
        super();

        this.result = result;
    }

    public MessageModel getMessageModel() {

        MessageModel messageModel = new MessageModel();
        messageModel.setMessage("Invalid fields");
        messageModel.setType(MessageModel.TYPE_ERROR);

        List<Pair<String, String>> details = new ArrayList<>();
        for (FieldError error : this.result.getFieldErrors()) {
            details.add(new Pair<>(Utility.capitalizeAndSeparate(error.getField()), error.getDefaultMessage()));
        }
        messageModel.setDetails(details);

        return messageModel;
    }

}
