package fr.altarik.toolbox.task;

public interface TaskI extends AutoCloseable {

    public void addTask(AltarikRunnable function) throws InterruptedException;

}
