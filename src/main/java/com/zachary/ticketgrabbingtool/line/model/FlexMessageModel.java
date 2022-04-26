package com.zachary.ticketgrabbingtool.line.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;
import com.zachary.ticketgrabbingtool.line.model.flex.IFlexMessageContent;

import java.util.List;

public class FlexMessageModel extends AbstractEntity implements IMessageModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "flex";

    private String type;
    private String altText;
    private IFlexMessageContent contents;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public IFlexMessageContent getContents() {
        return contents;
    }

    public void setContents(IFlexMessageContent contents) {
        this.contents = contents;
    }

}
