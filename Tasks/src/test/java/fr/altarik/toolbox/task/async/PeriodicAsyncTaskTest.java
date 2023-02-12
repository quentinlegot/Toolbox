package fr.altarik.toolbox.task.async;

import fr.altarik.toolbox.task.AltarikRunnable;
import org.junit.jupiter.api.Test;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class PeriodicAsyncTaskTest {

    @Test
    void testPeriodicASyncTask() {
        AsyncPeriodicTasks worker = (AsyncPeriodicTasks) AsyncPeriodicTasks.initialize();
        Stack<AtomicInteger> results = new Stack<>();
        AtomicInteger value1 = new AtomicInteger(1);
        AtomicInteger value2 = new AtomicInteger(2);
        AltarikRunnable runnable1 = new AltarikRunnable() {
            @Override
            public void run() {
                results.add(value1);
            }
        };
        AltarikRunnable runnable2 = new AltarikRunnable() {
            @Override
            public void run() {
                results.add(value2);
            }
        };


    }
}
