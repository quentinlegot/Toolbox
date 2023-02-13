package fr.altarik.toolbox.task.sync;

import fr.altarik.toolbox.task.AltarikRunnable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class PeriodicSyncTaskTest {

    @Test
    void testPeriodicSyncTask() {
        List<AtomicInteger> results = new ArrayList<>();
        PeriodicSyncTask worker = (PeriodicSyncTask) PeriodicSyncTask.initialize();
        AtomicInteger value1 = new AtomicInteger(1);
        AtomicInteger value2 = new AtomicInteger(2);
        worker.addTask(new AltarikRunnable() {

            @Override
            public void run() {
                results.add(value1);
            }
        }, 1, 3);
        worker.addTask(new AltarikRunnable() {
            private int i = 0;
            @Override
            public void run() {
                results.add(value2);
                if(i++ == 5)
                    this.cancel();
            }
        });
        for(int i = 0; i < 10; i++) {
            worker.scheduler.run();
        }
        AtomicInteger[] expected = {
                value2,
                value1,
                value2,
                value2,
                value2,
                value1,
                value2,
                value2,
                value1
        };
        Assertions.assertArrayEquals(expected, results.toArray());
    }

}
