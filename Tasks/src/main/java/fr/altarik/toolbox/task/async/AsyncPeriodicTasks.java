package fr.altarik.toolbox.task.async;

import fr.altarik.toolbox.task.*;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A task manager to execute periodic tasks asynchronously. A scheduler on the main/server thread will send the task to
 * worker threads.
 */
public class AsyncPeriodicTasks implements PeriodicTaskI, AsyncTaskI, SendTaskWorkerI {

    private final ExecutorService worker;
    private final Queue<SchedulerTaskData> tasks;
    protected final Scheduler scheduler;
    private final ServerTickListener listener;

    private AsyncPeriodicTasks(int numberOfWorker) {
        if(numberOfWorker == 1) {
            worker = Executors.newSingleThreadExecutor();
        } else if (numberOfWorker <= 0) {
            worker = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        } else {
            worker = Executors.newFixedThreadPool(numberOfWorker);
        }
        tasks = new ConcurrentLinkedQueue<>();
        this.scheduler = new Scheduler(this, tasks);
        this.listener = new ServerTickListener(scheduler);
    }

    /**
     * Call this method at startup or before first use of {@link AsyncTasks#addTask(AltarikRunnable)}, cause without it, nothing will work
     * This method declare worker thread and start it, without call it, by calling addTask(Runnable), it'll add your task to Queue, but tasks will never be consumed.
     *
     * @return an instance of AsyncTasks
     */
    public static PeriodicTaskI initialize(int numberOfWorker) {
        return new AsyncPeriodicTasks(numberOfWorker);
    }

    public static PeriodicTaskI initialize() {
        return initialize(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Send the task to the scheduler, the task is executed at the next server tick and at every following tick
     * @param function the function which will be executed
     * @throws InterruptedException if worker has terminated or is shutting down
     */
    @Override
    public void addTask(AltarikRunnable function) throws InterruptedException {
        this.addTask(function, 0, 1);
    }

    /**
     * Send the task to the scheduler, executed depending on the parameters (delay and period)
     * @param function the function to execute
     * @param delay delay in tick before starting the task
     * @param period time in tick to wait between runs
     * @throws InterruptedException if worker has terminated or is shutting down
     */
    @Override
    public void addTask(AltarikRunnable function, long delay, long period) throws InterruptedException {
        if(worker.isTerminated() || worker.isShutdown()) {
            throw new InterruptedException("Worker has been terminated or shutdown, it's impossible to add new task");
        }
        tasks.add(new SchedulerTaskData(function, delay, period - 1));
    }

    /**
     * Try to execute task you already send in 10 seconds, otherwise workers are killed.
     * @throws AsyncTasks.UnfinishedTasksException if workers has been shutdown before finishing every tasks
     */
    @Override
    public void close() throws Exception {
        worker.shutdown();
        boolean result = worker.awaitTermination(10, TimeUnit.SECONDS);
        if(!result) {
            worker.shutdownNow();
            throw new AsyncTasks.UnfinishedTasksException("Tasks take too many time to finish, shutdown has been enforce");
        }
    }

    @Override
    public void sendTask(AltarikRunnable task) {
        worker.submit(task);
    }
}
