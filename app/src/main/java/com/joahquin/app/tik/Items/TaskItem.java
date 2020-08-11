package com.joahquin.app.tik.Items;

import java.util.ArrayList;

public class TaskItem {

    int id;
    int assignment_id;
    long timeInterval;
    boolean isPending;
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

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
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
                ", timeInterval=" + timeInterval +
                ", isPending=" + isPending +
                ", stepList=" + stepList +
                '}';
    }
}
