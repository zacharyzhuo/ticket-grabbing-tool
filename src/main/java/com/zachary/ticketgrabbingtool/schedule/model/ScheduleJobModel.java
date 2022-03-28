package com.zachary.ticketgrabbingtool.schedule.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;

public abstract class ScheduleJobModel<T extends Job> extends AbstractEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final int maxPriority = 10;

    private Class<T> jobClass;
    private String identity;
    private String group;
    private int priority;
    private boolean isLoop;
    private int interval;
    private Date startTime;
    private CronScheduleBuilder cronScheduleBuilder;
    private Map<String, String> data;

    public ScheduleJobModel(Class<T> jobClass, String identity, String group, int priority, boolean isLoop,
                            int interval, Date startTime, CronScheduleBuilder cronScheduleBuilder) {
        this.jobClass = jobClass;
        this.identity = identity;
        this.group = group;
        this.priority = maxPriority - priority;
        this.isLoop = isLoop;
        this.interval = interval;
        this.startTime = startTime;
        this.cronScheduleBuilder = cronScheduleBuilder;
        this.data = new HashMap<String, String>();
    }

    public Class<T> getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class<T> jobClass) {
        this.jobClass = jobClass;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public CronScheduleBuilder getCronScheduleBuilder() {
        return cronScheduleBuilder;
    }

    public void setCronScheduleBuilder(CronScheduleBuilder cronScheduleBuilder) {
        this.cronScheduleBuilder = cronScheduleBuilder;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

}
