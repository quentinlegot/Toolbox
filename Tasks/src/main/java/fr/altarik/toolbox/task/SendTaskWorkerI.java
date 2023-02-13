package fr.altarik.toolbox.task;

public interface SendTaskWorkerI {

    /**
     * Internal use for scheduler, do not use.
     * Scheduler use this method to send the task to execute to worker
     * @param task task to execute now
     */
    void sendTask(AltarikRunnable task);
}
