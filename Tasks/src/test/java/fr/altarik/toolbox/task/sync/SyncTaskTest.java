package fr.altarik.toolbox.task.sync;

import fr.altarik.toolbox.task.AltarikRunnable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class SyncTaskTest {

    @Test
    void testOneTimeTask() {
        SyncTask worker = (SyncTask) SyncTask.initialize();
        List<AtomicInteger> results = new ArrayList<>();
        AtomicInteger value1 = new AtomicInteger(1);
        AtomicInteger value2 = new AtomicInteger(2);
        AltarikRunnable task1 = new AltarikRunnable() {
            @Override
            public void run() {
                results.add(value1);
            }
        };
        AltarikRunnable task2 = new AltarikRunnable() {
            @Override
            public void run() {
                results.add(value2);
            }
        };
        worker.addTask(task1);
        worker.run();
        worker.run();
        worker.addTask(task2);
        worker.addTask(task1);
        worker.addTask(task2);
        worker.run();
        worker.addTask(task1);
        worker.run();
        AtomicInteger[] expected = {
                value1,
                value2,
                value1,
                value2,
                value1
        };
        Assertions.assertArrayEquals(expected, results.toArray());
    }
}
