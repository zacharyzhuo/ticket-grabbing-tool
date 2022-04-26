package com.zachary.ticketgrabbingtool.line.model.flex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zachary.ticketgrabbingtool.json.AbstractEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BubbleModel extends AbstractEntity implements IFlexMessageContent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "bubble";

    private String type;
    private BoxModel body;
    private BoxModel footer;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BoxModel getBody() {
        return body;
    }

    public void setBody(BoxModel body) {
        this.body = body;
    }

    public BoxModel getFooter() {
        return footer;
    }

    public void setFooter(BoxModel footer) {
        this.footer = footer;
    }

}
