package com.joahquin.app.tik.Items;

import java.util.ArrayList;

public class AssignmentItem {
    int id;
    int type;
    String description;
    boolean reccursive;
    long interval;
    String createdOn;
    String lastPass;
    ArrayList<TaskItem> taskList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isReccursive() {
        return reccursive;
    }

    public void setReccursive(boolean reccursive) {
        this.reccursive = reccursive;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastPass() {
        return lastPass;
    }

    public void setLastPass(String lastPass) {
        this.lastPass = lastPass;
    }

    public ArrayList<TaskItem> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<TaskItem> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        return "AssignmentItem{" +
                "id=" + id +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", isReccursive=" + reccursive +
                ", interval=" + interval +
                ", createdOn='" + createdOn + '\'' +
                ", lastPass='" + lastPass + '\'' +
                ", taskList=" + taskList +
                '}';
    }
}
