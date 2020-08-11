package com.joahquin.app.tik.Items;

public class StepItem {
    int id;
    int task_id;
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

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
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
                ", task_id=" + task_id +
                ", info='" + info + '\'' +
                ", interval=" + interval +
                ", actionToTake=" + actionToTake +
                ", isPending=" + isPending +
                '}';
    }
}
