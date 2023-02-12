package fr.altarik.toolbox.task.sync;

import fr.altarik.toolbox.task.AltarikRunnable;
import fr.altarik.toolbox.task.SchedulerTaskData;
import fr.altarik.toolbox.task.TaskI;

import java.util.ArrayList;
import java.util.List;

public class SyncTask implements TaskI, Runnable {

    private final ServerTickListener listener;
    private final List<SchedulerTaskData> tasks;

    private SyncTask() {
        this.listener = new ServerTickListener(this);
        this.tasks = new ArrayList<>(2);
    }

    public static TaskI initialize() {
        return new SyncTask();
    }

    @Override
    public void addTask(AltarikRunnable function) {
        addTask(function, 0);
    }

    public void addTask(AltarikRunnable function, int delay) {
        tasks.add(new SchedulerTaskData(function, delay, 0));
    }

    @Override
    public void run() {
        List<SchedulerTaskData> removeList = new ArrayList<>(tasks.size());
        for(SchedulerTaskData data : tasks) {
            if(!data.getFunction().isCancelled()) {
                long currentDelay = data.getCurrentDelay();
                if(currentDelay != 0) {
                    data.setCurrentDelay(currentDelay - 1);
                } else {
                    data.getFunction().run();
                    removeList.add(data);
                }
            } else {
                removeList.add(data);
            }
        }
        for(SchedulerTaskData toRemove : removeList) {
            tasks.remove(toRemove);
        }
    }
}
