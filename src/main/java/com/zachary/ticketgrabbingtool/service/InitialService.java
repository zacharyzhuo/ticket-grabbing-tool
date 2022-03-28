package com.zachary.ticketgrabbingtool.service;

import com.zachary.ticketgrabbingtool.schedule.model.NewPostsCheckJobModel;
import com.zachary.ticketgrabbingtool.schedule.service.ScheduleDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class InitialService {

    private Logger logger = LoggerFactory.getLogger(InitialService.class);

    private static final boolean quartzClusterEnable = false;

    private static boolean newPostsCheckJobEnable = true;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        logger.info("InitialService init.");
        setupScheduleService();
    }

    @PreDestroy
    public void shutdownNow() {
        ScheduleDelegate.getInstance().shutdown();
        ScheduleDelegate.getInstance().dispose();
    }

    private void setupScheduleService() {
        // initial schedule application context
        ScheduleDelegate.getInstance().setApplicationContext(applicationContext);

        if (newPostsCheckJobEnable) {
            NewPostsCheckJobModel newPostsCheckJobModel = new NewPostsCheckJobModel();
            ScheduleDelegate.getInstance().addJob(newPostsCheckJobModel, !quartzClusterEnable);
        }
    }
}
