package com.zachary.ticketgrabbingtool.commime.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

public class UserModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private UserDetailModel user;

    public UserDetailModel getUser() {
        return user;
    }

    public void setUser(UserDetailModel user) {
        this.user = user;
    }

}
