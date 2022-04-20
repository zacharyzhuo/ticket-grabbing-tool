package com.zachary.ticketgrabbingtool.httpclient.model;

import com.zachary.ticketgrabbingtool.rent591.model.PostsModel;

public class HttpClientResultModel_Rent591 extends HttpClientResultModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String csrfToken;
    private PostsModel postsModel;

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public PostsModel getPostsModel() {
        return postsModel;
    }

    public void setPostsModel(PostsModel postsModel) {
        this.postsModel = postsModel;
    }

}
