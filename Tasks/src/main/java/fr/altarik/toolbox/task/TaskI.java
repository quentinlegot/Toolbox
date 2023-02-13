package fr.altarik.toolbox.task;

public interface TaskI {

    /**
     * Send task to worker, execution depends on implementation
     * @param function task you send to worker
     * @throws InterruptedException used by asynchronous workers if threads has been interrupted or shutdown
     */
    void addTask(AltarikRunnable function) throws InterruptedException;

}
