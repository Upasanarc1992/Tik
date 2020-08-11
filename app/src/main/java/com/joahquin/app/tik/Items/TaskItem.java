package com.joahquin.app.tik.Items;

import java.util.ArrayList;

public class TaskItem {

    int id;
    int assignment_id;
    String scheduleDateTime;
    long timeInterval;
    boolean isPending;
    boolean isActive;
    ArrayList<StepItem> stepList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(int assignment_id) {
        this.assignment_id = assignment_id;
    }

    public String getScheduleDateTime() {
        return scheduleDateTime;
    }

    public void setScheduleDateTime(String scheduleDateTime) {
        this.scheduleDateTime = scheduleDateTime;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ArrayList<StepItem> getStepList() {
        return stepList;
    }

    public void setStepList(ArrayList<StepItem> stepList) {
        this.stepList = stepList;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "id=" + id +
                ", assignment_id=" + assignment_id +
                ", scheduleDateTime='" + scheduleDateTime + '\'' +
                ", timeInterval=" + timeInterval +
                ", isPending=" + isPending +
                ", isActive=" + isActive +
                ", stepList=" + stepList +
                '}';
    }
}
