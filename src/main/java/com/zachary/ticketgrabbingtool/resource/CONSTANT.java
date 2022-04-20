package com.zachary.ticketgrabbingtool.resource;

public enum CONSTANT {
    USER_AGENT("user.agent"),

    COMMIME_URL("commime.url"),
    COMMIME_USER_LOCALECODE("commime.user.localeCode"),

    RENT_591("rent591.url"),
    RENT_591_URLJUMPIP("rent591.urlJumpIp"),

    QUARTZ_CLUSTER_ENABLE("quartz.cluster.enable"),
    SERVICE_NEWPOSTSCHECKJOB_ENABLE("service.newPostsCheckJob.enable"),
    NEWPOSTSCHECKJOB_INTERVAL("NewPostsCheckJob.interval"),

    LINE_BOT_CHANNEL_TOKEN("line.bot.channel-token"),
    LINE_BOT_ADMIN_USERID("line.bot.admin.userId"),
    LINE_BOT_WAN_USERID("line.bot.wan.userId"),
    LINE_BOT_URL("line.bot.url"),

    ;

    private final String str;

    CONSTANT(String str) {
        this.str = str;
    }

    public String toString() {
        return str;
    }
}
