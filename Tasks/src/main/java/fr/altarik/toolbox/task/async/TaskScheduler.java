package fr.altarik.toolbox.task.async;

import fr.altarik.toolbox.task.AltarikRunnable;
import fr.altarik.toolbox.task.SchedulerTaskData;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Vector;

public class TaskScheduler {
    private Vector<SchedulerTaskData> asyncTasks;

    /**
     * Return last time the method was executed, Value initialized to now when using sending the task to scheduler
     */
    private HashMap<SchedulerTaskData, Instant> lastTimeExecution;
    private boolean stop = false;


    public synchronized void sendAsyncTask(AltarikRunnable function, long delay, long period) throws InterruptedException {
        SchedulerTaskData data = new SchedulerTaskData(function, delay, period);
        asyncTasks.addElement(data);
        lastTimeExecution.put(new SchedulerTaskData(function, delay, period), Instant.now());
        notify();
    }

    /**
     * Function executed in asynchronous workers with periodic tasks
     */
    public synchronized void asyncRunnerPeriodicTasks() throws InterruptedException {
        loop: while(true) {
            notify();
            while(asyncTasks.size() == 0) {
                if(isStop()) {
                    break loop;
                }
                wait();
            }
            SchedulerTaskData data = asyncTasks.firstElement();
            asyncTasks.remove(data);
            if(!data.getFunction().isCancelled()) {
                long currentDelay = data.getCurrentDelay();
                Instant currentTime = Instant.now();
                Instant lastExecution = lastTimeExecution.get(data);
                // (lastExec + delay) - currentTime
                if(lastExecution.plus(currentDelay, ChronoUnit.MILLIS).isBefore(currentTime)) {
                    data.getFunction().run();
                    data.setCurrentDelay(data.getPeriod());
                    lastTimeExecution.put(data, Instant.now());
                    asyncTasks.addElement(data);
                }
            }
        }
    }

    public synchronized boolean isStop() {
        return stop;
    }

    public synchronized void setStop(boolean stop) {
        this.stop = stop;
    }

    public int getNumberOfTasks() {
        return asyncTasks.size();
    }
}
