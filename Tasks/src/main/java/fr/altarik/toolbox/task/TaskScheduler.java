package fr.altarik.toolbox.task;

import fr.altarik.toolbox.task.syncTasks.SchedulerTaskData;

import java.util.Vector;

public class TaskScheduler {
    private Vector<SchedulerTaskData> asyncTasks;
    private boolean stop = false;


    public synchronized void sendAsyncTask(AltarikRunnable function, long delay, long period) throws InterruptedException {
        asyncTasks.addElement(new SchedulerTaskData(function, delay, period));
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
                if(currentDelay != 0) {
                    data.setCurrentDelay(currentDelay - 1);
                    asyncTasks.addElement(data);
                } else {
                    data.getFunction().run();
                    data.setCurrentDelay(data.getPeriod());
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
