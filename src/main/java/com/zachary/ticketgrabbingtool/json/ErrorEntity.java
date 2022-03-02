package com.zachary.ticketgrabbingtool.json;

public class ErrorEntity extends AbstractEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String error;

    public ErrorEntity(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
