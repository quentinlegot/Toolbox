package fr.altarik.toolbox.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Scheduler implements Runnable {

    private final Queue<SchedulerTaskData> tasks;
    private final SendTaskWorkerI worker;

    public Scheduler(SendTaskWorkerI worker, Queue<SchedulerTaskData> tasks) {
        this.worker = worker;
        this.tasks = tasks;
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
                    worker.sendTask(data.getFunction());
                    if(data.getPeriod() == -1) {
                        removeList.add(data);
                    } else {
                        data.setCurrentDelay(data.getPeriod());
                    }
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
