package fr.altarik.toolbox.task.async;

import fr.altarik.toolbox.task.*;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncPeriodicTasks implements PeriodicTaskI, AsyncTaskI, SendTaskWorkerI {

    private final ExecutorService worker;
    private final Stack<SchedulerTaskData> tasks;
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
        tasks = new Stack<>();
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

    @Override
    public void addTask(AltarikRunnable function) throws InterruptedException {
        this.addTask(function, 0, 1000);
    }

    @Override
    public void addTask(AltarikRunnable function, long delay, long period) throws InterruptedException {
        if(worker.isTerminated() || worker.isShutdown()) {
            throw new InterruptedException("Worker has been terminated or shutdown, it's impossible to add new task");
        }
        tasks.add(new SchedulerTaskData(function, delay, period - 1));
    }

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
