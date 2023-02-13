package fr.altarik.toolbox.task;

import fr.altarik.toolbox.task.async.AsyncPeriodicTasks;
import fr.altarik.toolbox.task.async.AsyncTaskI;
import fr.altarik.toolbox.task.async.AsyncTasks;
import fr.altarik.toolbox.task.sync.PeriodicSyncTask;
import net.fabricmc.api.ModInitializer;

public class Task implements ModInitializer {

    public final TaskI asyncWorkers = AsyncTasks.initialize();
    public final PeriodicTaskI periodicSyncTask = PeriodicSyncTask.initialize();

    public final AsyncTaskI asyncTasks = AsyncTasks.initialize();

    public final PeriodicTaskI periodicAsyncTask = AsyncPeriodicTasks.initialize();

    @Override
    public void onInitialize() {
        /*try {
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
        }*/
    }

    public TaskI getAsyncWorkers() {
        return asyncWorkers;
    }
}