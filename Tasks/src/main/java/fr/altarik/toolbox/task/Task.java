package fr.altarik.toolbox.task;

import fr.altarik.toolbox.task.asyncTasks.AsyncTasks;
import net.fabricmc.api.ModInitializer;

public class Task implements ModInitializer {

    public TaskI asyncWorkers = AsyncTasks.initialize();

    @Override
    public void onInitialize() {

    }

    public TaskI getAsyncWorkers() {
        return asyncWorkers;
    }
}