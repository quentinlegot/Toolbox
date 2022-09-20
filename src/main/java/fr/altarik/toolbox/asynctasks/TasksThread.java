package fr.altarik.toolbox.asynctasks;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TasksThread {

    boolean isWaiting = false;
    final ReentrantLock lock = new ReentrantLock();
    final Condition lockSignal = lock.newCondition();
    Thread workerThread;

    final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

    public void run() {
        workerThread = new Thread(() -> {

            try {
                for(;;) {
                    while(tasks.isEmpty()) {
                        lock.lock();
                        isWaiting = true;
                        lockSignal.await();
                        isWaiting = false;
                        lock.unlock();
                    }
                    while(!tasks.isEmpty()) {
                        tasks.take().run();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        workerThread.start();

    }

}
