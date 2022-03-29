package com.zachary.ticketgrabbingtool.schedule.service;

import com.zachary.ticketgrabbingtool.resource.CONSTANT;
import com.zachary.ticketgrabbingtool.resource.ConfigReader;
import com.zachary.ticketgrabbingtool.schedule.model.ScheduleJobModel;
import com.zachary.ticketgrabbingtool.utils.DateUtil;

import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

public class ScheduleDelegate {

    private Logger logger = LoggerFactory.getLogger(ScheduleDelegate.class);

    private static final boolean quartzClusterEnable = ConfigReader.getBoolean(CONSTANT.QUARTZ_CLUSTER_ENABLE);

    private static ScheduleDelegate instance;

    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;

    private SchedulerFactory inMemorySchedulerFactory;
    private Scheduler inMemoryScheduler;

    private ScheduleDelegate() {
        try {
            inMemorySchedulerFactory = new StdSchedulerFactory("config/quartz-inmemory.properties");
            inMemoryScheduler = inMemorySchedulerFactory.getScheduler();
            inMemoryScheduler.start();

            if (quartzClusterEnable) {
                schedulerFactory = new StdSchedulerFactory("config/quartz.properties");
                scheduler = schedulerFactory.getScheduler();
                scheduler.start();
            }
        } catch (SchedulerException e) {
            logger.error("ScheduleDelegate Startup Error");
            logger.error(e.getMessage());
        }
    }

    public static ScheduleDelegate getInstance() {
        if (instance == null) {
            instance = new ScheduleDelegate();
        }
        return instance;
    }

    public void start() {
        if (inMemoryScheduler == null) {
            try {
                inMemoryScheduler = inMemorySchedulerFactory.getScheduler();
                inMemoryScheduler.start();
            } catch (SchedulerException e) {
                logger.error("ScheduleDelegate Startup Error");
                logger.error(e.getMessage());
            }
        }
        if (quartzClusterEnable && scheduler == null) {
            try {
                scheduler = schedulerFactory.getScheduler();
                scheduler.start();
            } catch (SchedulerException e) {
                logger.error("ScheduleDelegate Startup Error");
                logger.error(e.getMessage());
            }
        }
    }

    // 指定 SchedulerFactoryBean 使用自己建立的
    public void setApplicationContext(ApplicationContext applicationContext) {
        try {
            AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
            jobFactory.setApplicationContext(applicationContext);

            inMemoryScheduler.setJobFactory(jobFactory);

            if (quartzClusterEnable) {
                scheduler.setJobFactory(jobFactory);
            }
        } catch (SchedulerException e) {
            logger.error("ScheduleDelegate setApplicationContext Error");
            logger.error(e.getMessage());
        }
    }

    public void shutdown() {
        if (inMemoryScheduler != null) {
            try {
                inMemoryScheduler.shutdown();
            } catch (SchedulerException e) {
                logger.error("ScheduleDelegate shutdown Error");
                logger.error(e.getMessage());
            }
            inMemoryScheduler = null;
        }
        if (quartzClusterEnable && scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                logger.error("ScheduleDelegate shutdown Error");
                logger.error(e.getMessage());
            }
            scheduler = null;
        }
    }

    public void dispose() {
        schedulerFactory = null;
        instance = null;
    }

    public <T extends Job> Date addJob(ScheduleJobModel<T> scheduleJobModel, boolean isInMemory) {
        Scheduler sched = isInMemory ? inMemoryScheduler : (quartzClusterEnable ? scheduler : inMemoryScheduler);

        try {
            JobDetail job = newJob(scheduleJobModel.getJobClass())
                    .withIdentity("Job:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup()).build();

            for (Map.Entry<String, String> entry : scheduleJobModel.getData().entrySet()) {
                job.getJobDataMap().put(entry.getKey(), entry.getValue());
            }

            Trigger trigger;

            if (scheduleJobModel.isLoop()) {
                trigger = newTrigger()
                        .withIdentity("Trigger:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup())
                        .startNow()
                        .withSchedule(simpleSchedule().withIntervalInMilliseconds(scheduleJobModel.getInterval())
                                .repeatForever().withMisfireHandlingInstructionNowWithRemainingCount())
                        .withPriority(scheduleJobModel.getPriority()).build();
            } else {
                if (DateUtils.addSeconds(new Date(), 5).compareTo(scheduleJobModel.getStartTime()) > 0) {
                    trigger = newTrigger()
                            .withIdentity("Trigger:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup())
                            .startNow().withPriority(scheduleJobModel.getPriority()).build();
                } else {
                    trigger = newTrigger()
                            .withIdentity("Trigger:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup())
                            .startAt(scheduleJobModel.getStartTime()).withPriority(scheduleJobModel.getPriority())
                            .build();
                }
            }

            Date scheduleTime = sched.scheduleJob(job, trigger);

            logger.info("Job:" + scheduleJobModel.getIdentity() + " is scheduled. "
                    + DateUtil.defaultDateToString(scheduleTime));

            return scheduleTime;
        } catch (SchedulerException e) {
            logger.info("Job:" + scheduleJobModel.getIdentity() + " is failed to schedule.");
            logger.info(e.getMessage());

            TriggerKey trigerKey = new TriggerKey("Trigger:" + scheduleJobModel.getIdentity(),
                    scheduleJobModel.getGroup());
            try {
                if (sched.checkExists(trigerKey) && sched.getTriggerState(trigerKey) == TriggerState.ERROR) {
                    sched.resumeTrigger(trigerKey);
                }
            } catch (SchedulerException sex) {
                logger.info("Trigger:" + scheduleJobModel.getIdentity() + " is failed to resume.");
                logger.info(sex.getMessage());
            }

            return null;
        }
    }

    public <T extends Job> Date addCronJob(ScheduleJobModel<T> scheduleJobModel, boolean isInMemory) {
        Scheduler sched = isInMemory ? inMemoryScheduler : (quartzClusterEnable ? scheduler : inMemoryScheduler);

        try {
            JobDetail job = newJob(scheduleJobModel.getJobClass())
                    .withIdentity("Job:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup()).build();

            for (Map.Entry<String, String> entry : scheduleJobModel.getData().entrySet()) {
                job.getJobDataMap().put(entry.getKey(), entry.getValue());
            }

            Trigger trigger;

            trigger = newTrigger()
                    .withIdentity("Trigger:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup()).startNow()
                    .withSchedule(scheduleJobModel.getCronScheduleBuilder())
                    .withPriority(scheduleJobModel.getPriority()).build();

            Date scheduleTime = sched.scheduleJob(job, trigger);

            logger.info("Job:" + scheduleJobModel.getIdentity() + " is scheduled. "
                    + DateUtil.defaultDateToString(scheduleTime));

            return scheduleTime;
        } catch (SchedulerException e) {
            logger.info("Job:" + scheduleJobModel.getIdentity() + " is failed to schedule.");
            logger.info(e.getMessage());

            TriggerKey trigerKey = new TriggerKey("Trigger:" + scheduleJobModel.getIdentity(),
                    scheduleJobModel.getGroup());
            try {
                if (sched.checkExists(trigerKey) && sched.getTriggerState(trigerKey) == TriggerState.ERROR) {
                    sched.resumeTrigger(trigerKey);
                }
            } catch (SchedulerException sex) {
                logger.info("Trigger:" + scheduleJobModel.getIdentity() + " is failed to resume.");
                logger.info(sex.getMessage());
            }

            return null;
        }
    }

    public <T extends Job> Date removeJob(ScheduleJobModel<T> scheduleJobModel, boolean isInMemory) {
        Scheduler sched = isInMemory ? inMemoryScheduler : (quartzClusterEnable ? scheduler : inMemoryScheduler);

        try {
            boolean unscheduleResult = sched.unscheduleJob(
                    triggerKey("Trigger:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup()));

            if (!unscheduleResult) {
                throw new SchedulerException();
            }

            sched.deleteJob(jobKey("Job:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup()));

            Date unscheduleTime = new Date();

            logger.info("Job:" + scheduleJobModel.getIdentity() + " is unscheduled. "
                    + DateUtil.defaultDateToString(unscheduleTime));

            return unscheduleTime;
        } catch (SchedulerException e) {
            logger.info("Job:" + scheduleJobModel.getIdentity() + " is failed to unschedule.");
            logger.info(e.getMessage());

            return null;
        }
    }

    public <T extends Job> Date interruptJob(ScheduleJobModel<T> scheduleJobModel, boolean isInMemory) {
        Scheduler sched = isInMemory ? inMemoryScheduler : (quartzClusterEnable ? scheduler : inMemoryScheduler);

        try {
            boolean interruptResult = sched
                    .interrupt(jobKey("Job:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup()));

            if (!interruptResult) {
                throw new SchedulerException();
            }

            boolean unscheduleResult = sched.unscheduleJob(
                    triggerKey("Trigger:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup()));

            if (!unscheduleResult) {
                throw new SchedulerException();
            }

            sched.deleteJob(jobKey("Job:" + scheduleJobModel.getIdentity(), scheduleJobModel.getGroup()));

            Date unscheduleTime = new Date();

            logger.info("Job:" + scheduleJobModel.getIdentity() + " is interrupt. "
                    + DateUtil.defaultDateToString(unscheduleTime));

            return unscheduleTime;
        } catch (SchedulerException e) {
            logger.info("Job:" + scheduleJobModel.getIdentity() + " is failed to interrupt.");
            logger.info(e.getMessage());

            return null;
        }
    }

}
