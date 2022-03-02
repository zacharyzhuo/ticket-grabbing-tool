package com.zachary.ticketgrabbingtool.commime.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

public class UserDetailModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String locale_code;
    private String password;
    private String mobile_phone_or_email;

    public String getLocale_code() {
        return locale_code;
    }

    public void setLocale_code(String locale_code) {
        this.locale_code = locale_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_phone_or_email() {
        return mobile_phone_or_email;
    }

    public void setMobile_phone_or_email(String mobile_phone_or_email) {
        this.mobile_phone_or_email = mobile_phone_or_email;
    }

}
