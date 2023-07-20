package fr.altarik.toolbox.task.server;

import fr.altarik.toolbox.task.PeriodicTaskI;
import fr.altarik.toolbox.task.TaskI;
import fr.altarik.toolbox.task.async.AsyncPeriodicTasks;
import fr.altarik.toolbox.task.async.AsyncTaskI;
import fr.altarik.toolbox.task.async.AsyncTasks;
import fr.altarik.toolbox.task.sync.PeriodicSyncTask;
import net.fabricmc.api.DedicatedServerModInitializer;

public class DedicatedServerTask implements DedicatedServerModInitializer {

    @SuppressWarnings("unused")
    public final TaskI asyncWorkers = AsyncTasks.initialize();
    @SuppressWarnings("unused")
    public final PeriodicTaskI periodicSyncTask = PeriodicSyncTask.initialize();
    @SuppressWarnings("unused")
    public final AsyncTaskI asyncTasks = AsyncTasks.initialize();
    @SuppressWarnings("unused")
    public final PeriodicTaskI periodicAsyncTask = AsyncPeriodicTasks.initialize();

    @Override
    public void onInitializeServer() {
        /* try {
            asyncWorkers.addTask(new AltarikRunnable() {
                @Override
                public void run() {
                    System.out.println("Hello world 1");
                }
            });
            periodicSyncTask.addTask(new AltarikRunnable() {
                @Override
                public void run() {
                    System.out.println("Hello world 2");
                }
            }, 40, 60);
            asyncTasks.addTask(new AltarikRunnable() {
                @Override
                public void run() {
                    System.out.println("Hello world 3 : " + Thread.currentThread().getName());
                }
            });
            periodicAsyncTask.addTask(new AltarikRunnable() {
                @Override
                public void run() {
                    System.out.println("Hello world 4 : " + Thread.currentThread().getName());
                }
            }, 60, 80);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } */
    }

    @SuppressWarnings("unused")
    public TaskI getAsyncWorkers() {
        return asyncWorkers;
    }
}