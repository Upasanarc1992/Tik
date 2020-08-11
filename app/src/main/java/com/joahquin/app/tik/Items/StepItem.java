package com.joahquin.app.tik.Items;

public class StepItem {
    int id;
    int taskId;
    String info;
    long interval;
    int actionToTake;
    boolean isPending;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getActionToTake() {
        return actionToTake;
    }

    public void setActionToTake(int actionToTake) {
        this.actionToTake = actionToTake;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    @Override
    public String toString() {
        return "StepItem{" +
                "id=" + id +
                ", task_id=" + taskId +
                ", info='" + info + '\'' +
                ", interval=" + interval +
                ", actionToTake=" + actionToTake +
                ", isPending=" + isPending +
                '}';
    }
}
