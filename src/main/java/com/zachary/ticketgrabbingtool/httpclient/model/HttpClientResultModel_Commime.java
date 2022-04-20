package com.zachary.ticketgrabbingtool.httpclient.model;

public class HttpClientResultModel_Commime extends HttpClientResultModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String csrfToken;

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

}
