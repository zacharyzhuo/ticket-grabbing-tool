package com.zachary.ticketgrabbingtool.line.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.List;

public class TextMessageModel extends AbstractEntity implements IMessageModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "text";

    private String type;
    private String text;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
