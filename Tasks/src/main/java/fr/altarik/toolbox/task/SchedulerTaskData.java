package fr.altarik.toolbox.task;

public class SchedulerTaskData {

    /**
     * Delay before executing the function for the first time
     * Correspond to tick in synchronous context and milliseconds in asynchronous context
     */
    private final long delay;
    /**
     * Period of time before re-executing the function
     * Correspond to tick in synchronous context and milliseconds in asynchronous context
     */
    private final long period;
    private final AltarikRunnable function;

    private long currentDelay;

    /**
     *
     * Delay and Period times corresponds to tick in synchronous context and milliseconds in asynchronous context
     *
     * @param function instructions to execute
     * @param delay Delay before executing the function for the first time
     * @param period Period of time before re-executing the function
     */
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
