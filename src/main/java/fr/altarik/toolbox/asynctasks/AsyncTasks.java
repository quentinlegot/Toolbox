package fr.altarik.toolbox.asynctasks;

public class AsyncTasks {

    private static final TasksThread worker = new TasksThread();

    public static void initialize() {
        worker.run();
    }

    public static void addTask(Runnable function) {
        worker.lock.lock();
        if(worker.workerThread.isInterrupted())
            throw new RuntimeException("Async task thread has been interrupted while waiting for another task, which is anormal, please report this issue to developers");
        worker.tasks.push(function);
        worker.lockSignal.signalAll();
        worker.lock.unlock();
    }

    public static int numberOfWaitingTask() {
        synchronized (worker.lock) {
            return worker.tasks.size();
        }
    }

}
