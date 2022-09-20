package fr.altarik.toolbox.asynctasks;

/**
 * A non-blocking small sized time-consuming tasks to executed asynchronously, this was developed mainly to be used to avoid to block main threads with mysql requests in mind
 */
public class AsyncTasks {

    private static final TasksThread worker = new TasksThread();

    /**
     * Call this method at startup or before first use of {@link AsyncTasks#addTask(Runnable)}, cause without it, nothing will work
     * This method declare worker thread and start it, without call it, by calling addTask(Runnable), it'll add your task to Queue, but tasks will never be consumed.
     */
    public static void initialize() {
        worker.run();
    }

    /**
     * <p>Method used to add your task to a list of task, task are stored in a FIFO(First-In First-Out) implementation ({@link java.util.concurrent.BlockingQueue}).
     * As BlockingQueue is a synchronized class, all operations to add or remove elements inside cannot have collisions issues</p>
     * <p>Example: </p>
     * <pre>
     *      for(int i = 0; i &lt; 4; i++) {
     *          System.out.println("task " + i);
     *          AtomicInteger atomicI = new AtomicInteger(i);
     *          AsyncTasks.addTask(() -> {
     *          System.out.println(i);
     *          }
     *      }
     *
     * </pre>
     * <p> Result(output may differ): </p>
     * <pre>
     *     task 0
     *     task 1
     *     0
     *     task 2
     *     1
     *     2
     *     task 3
     *     3
     * </pre>
     * The worker thread is sleeping if it doesn't have task to execute and wake up if necessary when you add a task
     * @param function task to be executed
     * @throws InterruptedException when worker thread or BlockQueue has been interrupted while waiting (which is anormal)
     */
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

    /**
     *  Return the numbers of produced tasks which hasn't been consumed yet
     * @return numbers of produced tasks which hasn't been consumed yet
     */
    public static int numberOfWaitingTask() {
        return worker.tasks.size();
    }

}
