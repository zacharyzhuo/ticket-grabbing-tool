package com.zachary.ticketgrabbingtool.schedule.model;

import com.zachary.ticketgrabbingtool.schedule.job.NewPostsCheckJob;

public class NewPostsCheckJobModel extends ScheduleJobModel<NewPostsCheckJob> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static int newPostsCheckInterval = 3000;

    public NewPostsCheckJobModel() {
        super(NewPostsCheckJob.class, NewPostsCheckJob.class.getName(), "PushGroup",
                NewPostsCheckJob.priority, true, newPostsCheckInterval, null, null);
    }
}
