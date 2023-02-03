package fr.altarik.toolbox.task.sync;

import fr.altarik.toolbox.task.AltarikRunnable;
import fr.altarik.toolbox.task.PeriodicTaskI;
import fr.altarik.toolbox.task.SchedulerTaskData;
import fr.altarik.toolbox.task.TaskI;

import java.util.ArrayList;
import java.util.List;

public class PeriodicSyncTask implements PeriodicTaskI, Runnable {

    private ServerTickListener listener;
    private List<SchedulerTaskData> tasks;

    private PeriodicSyncTask() {
        this.listener = new ServerTickListener(this);
        this.tasks = new ArrayList<>(2);
    }


    public static TaskI initialize() {
        return new PeriodicSyncTask();
    }

    @Override
    public void addTask(AltarikRunnable function) throws InterruptedException {
        addTask(function, 0, 1);
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
                    data.setCurrentDelay(data.getPeriod());
                }
            } else {
                removeList.add(data);
            }
        }
        for(SchedulerTaskData toRemove : removeList) {
            tasks.remove(toRemove);
        }

    }

    @Override
    public void addTask(AltarikRunnable function, long delay, long period) throws InterruptedException {
        tasks.add(new SchedulerTaskData(function, delay, period));
    }
}
