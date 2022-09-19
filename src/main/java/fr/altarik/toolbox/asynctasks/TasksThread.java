package fr.altarik.toolbox.asynctasks;

import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TasksThread {

    final ReentrantLock lock = new ReentrantLock();
    final Condition lockSignal = lock.newCondition();
    Thread workerThread;

    final Stack<Runnable> tasks = new Stack<>();

    public void run() {
        workerThread = new Thread(() -> {
            lock.lock();
            try {
                for(;;) {
                    while(tasks.empty()) {
                        lockSignal.await();
                    }
                    while(!tasks.empty()) {
                        tasks.pop().run();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            lock.unlock();
        });
        workerThread.start();

    }

}
