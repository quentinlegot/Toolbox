package fr.altarik.toolbox.task.async;

import fr.altarik.toolbox.task.AltarikRunnable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class PeriodicAsyncTaskTest {

    @Test
    void testPeriodicASyncTask() throws Exception {
        AsyncPeriodicTasks worker = (AsyncPeriodicTasks) AsyncPeriodicTasks.initialize(1);
        Stack<AtomicInteger> results = new Stack<>();
        AtomicInteger value1 = new AtomicInteger(1);
        AtomicInteger value2 = new AtomicInteger(2);
        AltarikRunnable runnable1 = new AltarikRunnable() {
            private int i = 0;
            @Override
            public void run() {
                results.add(value1);
                i++;
                if(i == 2)
                    cancel();
            }
        };
        AltarikRunnable runnable2 = new AltarikRunnable() {
            private int i = 0;
            @Override
            public void run() {
                results.add(value2);
                i++;
                if(i == 4)
                    cancel();
            }
        };
        worker.addTask(runnable1, 1, 4);
        worker.addTask(runnable2, 0, 2);
        for(int i = 0; i < 10; i++) {
            worker.scheduler.run();
        }
        AtomicInteger[] expected = {
                value2,
                value1,
                value2,
                value2,
                value1,
                value2,
                value2,
                value1
        };
        worker.close();
        Assertions.assertArrayEquals(expected, results.toArray());

    }
}
