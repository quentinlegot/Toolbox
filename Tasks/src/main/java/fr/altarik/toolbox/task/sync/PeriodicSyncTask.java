package fr.altarik.toolbox.task.sync;

import fr.altarik.toolbox.task.*;

import java.util.ArrayList;
import java.util.List;

public class PeriodicSyncTask implements PeriodicTaskI, SendTaskWorkerI {

    private final ServerTickListener listener;
    private final List<SchedulerTaskData> tasks;
    protected final Scheduler scheduler;

    private PeriodicSyncTask() {
        this.tasks = new ArrayList<>(2);
        this.scheduler = new Scheduler(this, tasks);
        this.listener = new ServerTickListener(scheduler);
    }


    public static PeriodicTaskI initialize() {
        return new PeriodicSyncTask();
    }

    @Override
    public void addTask(AltarikRunnable function) {
        addTask(function, 0, 1);
    }

    @Override
    public void addTask(AltarikRunnable function, long delay, long period) {
        tasks.add(new SchedulerTaskData(function, delay, period - 1));
    }

    @Override
    public void sendTask(AltarikRunnable task) {
        task.run();
    }
}
