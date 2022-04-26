package com.zachary.ticketgrabbingtool.rent591.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.List;

public class PostsModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int A_BATCH_POST_UNIT = 30;

    private List<PostModel> posts;
    private int firstRow;
    private int totalRows;

    public List<PostModel> getPosts() {
        return posts;
    }

    public void setPosts(List<PostModel> posts) {
        this.posts = posts;
    }

    public void addPost(List<PostModel> post) {
        this.posts.addAll(post);
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

}
