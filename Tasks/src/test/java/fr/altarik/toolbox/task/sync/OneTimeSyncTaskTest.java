package fr.altarik.toolbox.task.sync;

import fr.altarik.toolbox.task.AltarikRunnable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OneTimeSyncTaskTest {

    @Test
    void testOneTimeTask() {
        OneTimeSyncTask worker = (OneTimeSyncTask) OneTimeSyncTask.initialize();
        List<AtomicInteger> results = new ArrayList<>();
        AtomicInteger value1 = new AtomicInteger(1);
        AtomicInteger value2 = new AtomicInteger(2);
        worker.addTask(new AltarikRunnable() {
            @Override
            public void run() {
                results.add(value1);
            }
        });
    }
}
