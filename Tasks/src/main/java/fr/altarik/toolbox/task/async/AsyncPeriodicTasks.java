package fr.altarik.toolbox.task.async;

import fr.altarik.toolbox.task.AltarikRunnable;
import fr.altarik.toolbox.task.PeriodicTaskI;
import fr.altarik.toolbox.task.TaskI;
import it.unimi.dsi.fastutil.ints.IntComparators;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncPeriodicTasks implements PeriodicTaskI, AsyncTaskI {

    private final ExecutorService worker;
    private final List<TaskScheduler> schedulers;

    private AsyncPeriodicTasks(int numberOfWorker) {
        int size = 0;
        if(numberOfWorker == 1) {
            worker = Executors.newSingleThreadExecutor();
            size = 1;
        } else if (numberOfWorker <= 0) {
            worker = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            size = Runtime.getRuntime().availableProcessors();
        } else {
            worker = Executors.newFixedThreadPool(numberOfWorker);
            size = numberOfWorker;
        }
        this.schedulers = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            TaskScheduler scheduler = new TaskScheduler();
            schedulers.add(scheduler);
            worker.submit(() -> {
                try {
                    scheduler.asyncRunnerPeriodicTasks();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    /**
     * Call this method at startup or before first use of {@link AsyncTasks#addTask(AltarikRunnable)}, cause without it, nothing will work
     * This method declare worker thread and start it, without call it, by calling addTask(Runnable), it'll add your task to Queue, but tasks will never be consumed.
     *
     * @return an instance of AsyncTasks
     */
    public static TaskI initialize(int numberOfWorker) {
        return new AsyncPeriodicTasks(numberOfWorker);
    }

    public static TaskI initialize() {
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
        schedulers.stream()
                .min((o1, o2) -> IntComparators.NATURAL_COMPARATOR.compare(o1.getNumberOfTasks(), o2.getNumberOfTasks()))
                .orElseThrow()
                .sendAsyncTask(function, delay, period);
    }

    @Override
    public void close() throws Exception {
        schedulers.forEach(s -> s.setStop(true));
        worker.shutdown();
        boolean result = worker.awaitTermination(10, TimeUnit.SECONDS);
        if(!result) {
            worker.shutdownNow();
            throw new AsyncTasks.UnfinishedTasksException("Tasks take too many time to finish, shutdown has been enforce");
        }
    }
}
