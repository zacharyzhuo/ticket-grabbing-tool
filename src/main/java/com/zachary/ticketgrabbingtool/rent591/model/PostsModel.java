package com.zachary.ticketgrabbingtool.rent591.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.List;

public class PostsModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int A_BATCH_POST_UNIT = 30;

    private List<PostModel> post;
    private int firstRow;
    private int totalRows;

    public List<PostModel> getPost() {
        return post;
    }

    public void setPost(List<PostModel> post) {
        this.post = post;
    }

    public void addPost(List<PostModel> post) {
        this.post.addAll(post);
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
