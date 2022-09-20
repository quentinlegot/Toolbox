package fr.altarik.toolbox.asynctasks;

public class AsyncTasks {

    private static final TasksThread worker = new TasksThread();

    public static void initialize() {
        worker.run();
    }

    public static void addTask(Runnable function) throws InterruptedException {
        if(worker.workerThread.isInterrupted())
            throw new InterruptedException("Async task thread has been interrupted while waiting for another task, which is anormal, please report this issue to developers");
        worker.tasks.put(function);
        // this condition is non-atomic, but we want to avoid unwanted and useless interruption in the main thread(s) while waiting for the worker thread to be released
        if(worker.isWaiting) {
            worker.lock.lock();
            worker.lockSignal.signalAll();
            worker.lock.unlock();
        }
    }

    public static int numberOfWaitingTask() {
        return worker.tasks.size();
    }

}
