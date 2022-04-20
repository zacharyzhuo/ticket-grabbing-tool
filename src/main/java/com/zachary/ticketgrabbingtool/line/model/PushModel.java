package com.zachary.ticketgrabbingtool.line.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.List;

public class PushModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String to;
    private List<IMessageModel> messages;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<IMessageModel> getMessages() {
        return messages;
    }

    public void setMessages(List<IMessageModel> messages) {
        this.messages = messages;
    }

}
