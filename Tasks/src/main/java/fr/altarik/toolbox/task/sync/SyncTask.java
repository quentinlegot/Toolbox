package fr.altarik.toolbox.task.sync;

import fr.altarik.toolbox.task.*;

import java.util.ArrayList;
import java.util.List;

public class SyncTask implements TaskI, SendTaskWorkerI {

    private final ServerTickListener listener;
    private final List<SchedulerTaskData> tasks;
    protected final Scheduler scheduler;

    private SyncTask() {
        this.tasks = new ArrayList<>(2);
        this.scheduler = new Scheduler(this, tasks);
        this.listener = new ServerTickListener(scheduler);
    }

    public static TaskI initialize() {
        return new SyncTask();
    }

    @Override
    public void addTask(AltarikRunnable function) {
        addTask(function, 0);
    }

    public void addTask(AltarikRunnable function, int delay) {
        tasks.add(new SchedulerTaskData(function, delay, -1));
    }

    @Override
    public void sendTask(AltarikRunnable task) {
        task.run();
    }
}
