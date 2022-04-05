package com.zachary.ticketgrabbingtool.httpclient.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;
import org.apache.http.client.protocol.HttpClientContext;

import org.apache.http.HttpResponse;

public abstract class HttpClientResultModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private HttpResponse response;
    private HttpClientContext context;

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public HttpClientContext getContext() {
        return context;
    }

    public void setContext(HttpClientContext context) {
        this.context = context;
    }

}
