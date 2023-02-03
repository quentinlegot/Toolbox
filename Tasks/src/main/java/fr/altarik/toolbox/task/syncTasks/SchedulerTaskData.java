package fr.altarik.toolbox.task.syncTasks;

import fr.altarik.toolbox.task.AltarikRunnable;

public class SchedulerTaskData {

    private final long delay;
    private final long period;
    private final AltarikRunnable function;

    private long currentDelay;

    public SchedulerTaskData(AltarikRunnable function, long delay, long period) {
        this.function = function;
        this.delay = delay;
        this.period = period;
        this.currentDelay = delay;
    }

    public AltarikRunnable getFunction() {
        return function;
    }

    public long getCurrentDelay() {
        return currentDelay;
    }

    public void setCurrentDelay(long currentDelay) {
        this.currentDelay = currentDelay;
    }

    public long getDelay() {
        return delay;
    }

    public long getPeriod() {
        return period;
    }
}
