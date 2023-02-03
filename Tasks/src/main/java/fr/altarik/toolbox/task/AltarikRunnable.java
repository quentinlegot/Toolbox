package fr.altarik.toolbox.task;

public abstract class AltarikRunnable implements Runnable {

    private boolean isCancelled = false;

    /**
     * Warning: Some task cannot be cancelled (mostly async tasks like {@link fr.altarik.toolbox.task.asyncTasks.AsyncTasks}
     * The result of this call is ignored in this case, you can still add a way to not execute its content (like if(isCancelled) return;)
     */
    public void cancel() {
        this.isCancelled = true;
    }

    public boolean isCancelled() {
        return isCancelled;
    }
}
