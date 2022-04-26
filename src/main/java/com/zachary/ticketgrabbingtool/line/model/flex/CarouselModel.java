package com.zachary.ticketgrabbingtool.line.model.flex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.List;

public class CarouselModel extends AbstractEntity implements IFlexMessageContent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "carousel";

    private String type;
    private List<BubbleModel> contents;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<BubbleModel> getContents() {
        return contents;
    }

    public void setContents(List<BubbleModel> contents) {
        this.contents = contents;
    }

}
