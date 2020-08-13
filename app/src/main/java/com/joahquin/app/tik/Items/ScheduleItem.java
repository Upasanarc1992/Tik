package com.joahquin.app.tik.Items;

import java.util.Date;

public class ScheduleItem {

    int id;
    int assignmentType;
    int taskId;
    int stepId;
    String alarmInfo;
    String alarmStepInfo;
    boolean isDone;
    long timeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(int assignmentType) {
        this.assignmentType = assignmentType;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getAlarmInfo() {
        return alarmInfo;
    }

    public void setAlarmInfo(String alarmInfo) {
        this.alarmInfo = alarmInfo;
    }

    public String getAlarmStepInfo() {
        return alarmStepInfo;
    }

    public void setAlarmStepInfo(String alarmStepInfo) {
        this.alarmStepInfo = alarmStepInfo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "ScheduleItem{" +
                "id=" + id +
                ", assignmentType=" + assignmentType +
                ", taskId=" + taskId +
                ", stepId=" + stepId +
                ", alarmInfo='" + alarmInfo + '\'' +
                ", alarmStepInfo='" + alarmStepInfo + '\'' +
                ", isDone=" + isDone +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
