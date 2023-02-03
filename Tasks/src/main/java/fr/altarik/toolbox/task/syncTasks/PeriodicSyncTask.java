package fr.altarik.toolbox.task.syncTasks;

import fr.altarik.toolbox.task.AltarikRunnable;
import fr.altarik.toolbox.task.TaskI;

import java.util.ArrayList;
import java.util.List;

public class PeriodicSyncTask implements TaskI, Runnable {

    private ServerTickListener listener;
    private List<AltarikRunnable> tasks;

    private PeriodicSyncTask() {
        this.listener = new ServerTickListener(this);
        this.tasks = new ArrayList<>(2);
    }


    public static TaskI initialize() {
        return new PeriodicSyncTask();
    }

    @Override
    public void addTask(AltarikRunnable function) throws InterruptedException {
        tasks.add(function);
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void run() {
        List<AltarikRunnable> removeList = new ArrayList<>(tasks.size());
        for(AltarikRunnable task : tasks) {
            if(task.isCancelled()) {
                removeList.add(task);
            } else {

                task.run();
            }
        }
        tasks.removeAll(removeList);
    }
}
