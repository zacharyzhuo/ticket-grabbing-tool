package com.zachary.ticketgrabbingtool.schedule.model;

import com.zachary.ticketgrabbingtool.resource.CONSTANT;
import com.zachary.ticketgrabbingtool.resource.ConfigReader;
import com.zachary.ticketgrabbingtool.schedule.job.NewPostsCheckJob;

public class NewPostsCheckJobModel extends ScheduleJobModel<NewPostsCheckJob> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final int newPostsCheckInterval = ConfigReader.getInteger(CONSTANT.NEWPOSTSCHECKJOB_INTERVAL);

    public NewPostsCheckJobModel() {
        super(NewPostsCheckJob.class, NewPostsCheckJob.class.getName(), "PushGroup",
                NewPostsCheckJob.priority, true, newPostsCheckInterval, null, null);
    }
}
