package com.zachary.ticketgrabbingtool.resource;

public enum CONSTANT {
    COMMIME_URL("commime.url"),
    COMMIME_USER_LOCALECODE("commime.user.localeCode"),

    QUARTZ_CLUSTER_ENABLE("quartz.cluster.enable"),
    SERVICE_NEWPOSTSCHECKJOB_ENABLE("service.newPostsCheckJob.enable"),
    NEWPOSTSCHECKJOB_INTERVAL("NewPostsCheckJob.interval")
    ;

    private final String str;

    CONSTANT(String str) {
        this.str = str;
    }

    public String toString() {
        return str;
    }
}
