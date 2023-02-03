package fr.altarik.toolbox.task.async;

import fr.altarik.toolbox.task.AltarikRunnable;
import fr.altarik.toolbox.task.TaskI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A non-blocking small sized time-consuming tasks to executed asynchronously.
 */
public class AsyncTasks implements TaskI {

    private final ExecutorService worker;

    private AsyncTasks(int numberOfWorker) {
        if(numberOfWorker == 1) {
            worker = Executors.newSingleThreadExecutor();
        } else if (numberOfWorker <= 0) {
            worker = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        } else {
            worker = Executors.newFixedThreadPool(numberOfWorker);
        }
    }

    /**
     * Call this method at startup or before first use of {@link AsyncTasks#addTask(AltarikRunnable)}, cause without it, nothing will work
     * This method declare worker thread and start it, without call it, by calling addTask(Runnable), it'll add your task to Queue, but tasks will never be consumed.
     *
     * @return an instance of AsyncTasks
     */
    public static TaskI initialize(int numberOfWorker) {
        return new AsyncTasks(numberOfWorker);
    }

    public static TaskI initialize() {
        return initialize(Runtime.getRuntime().availableProcessors());
    }

    /**
     * <p>Method used to add your task to a list of task, task are stored in a FIFO(First-In First-Out) implementation ({@link java.util.concurrent.LinkedBlockingQueue}).
     * As LinkedBlockingQueue is a synchronized class, all operations to add or remove elements inside cannot have collisions issues</p>
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
     * The worker thread is sleeping if it doesn't have task to execute and wake up if necessary when you add a task.
     * A worker which crash due to exception in the code of your task will automatically be recreated (see {@link java.util.concurrent.ThreadPoolExecutor} for more informations).
     * @param function task to be executed
     * @throws InterruptedException when worker thread or BlockQueue has been interrupted while waiting (which is anormal)
     */
    public void addTask(AltarikRunnable function) throws InterruptedException {
        if(worker.isTerminated() || worker.isShutdown()) {
            throw new InterruptedException("Worker has been terminated or shutdown, it's impossible to add new task");
        }
        worker.submit(function);
    }

    /**
     * This method is call when you want to close workers and wait for waiting tasks to finish
     *
     * @throws UnfinishedTasksException when all tasks cannot be terminated in 10 seconds
     * @throws InterruptedException if interrupted while waiting for tasks to finish
     */
    @Override
    public void close() throws UnfinishedTasksException, InterruptedException {
        worker.shutdown();
        boolean result = worker.awaitTermination(10, TimeUnit.SECONDS);
        if(!result) {
            worker.shutdownNow();
            throw new UnfinishedTasksException("Tasks take too many time to finish, shutdown has been enforce");
        }
    }

    public static class UnfinishedTasksException extends Exception {

        public UnfinishedTasksException(String message) {
            super(message);
        }

    }
}
