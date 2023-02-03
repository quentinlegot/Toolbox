package fr.altarik.toolbox.task;

public interface PeriodicTaskI extends TaskI {

    /**
     * Run a task periodically
     * @param function the function to execute
     * @param delay delay before starting the task
     * @param period time to wait between runs
     * @throws InterruptedException When executed asynchronously, task may be interrupted
     * @see fr.altarik.toolbox.task.syncTasks.PeriodicSyncTask
     */
    public void addTask(AltarikRunnable function, long delay, long period) throws InterruptedException;
}
