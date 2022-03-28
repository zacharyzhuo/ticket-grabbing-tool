package com.zachary.ticketgrabbingtool.schedule.job;

import com.zachary.ticketgrabbingtool.rent591.service.Rent591Service;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class NewPostsCheckJob implements org.quartz.Job {

    private Logger logger = LoggerFactory.getLogger(NewPostsCheckJob.class);

    public static final int priority = 5;

    @Autowired
    Rent591Service rent591Service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("NewPostsCheckJob: " + context.getJobDetail().getKey().getName() + " start.");
        logger.info("[gggggggggg]");
    }

}
