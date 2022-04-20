package com.zachary.ticketgrabbingtool.line.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

public class EventModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
