package fr.altarik.toolbox;

import fr.altarik.toolbox.task.AltarikRunnable;
import fr.altarik.toolbox.task.TaskI;
import fr.altarik.toolbox.task.asyncTasks.AsyncTasks;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class AsyncTaskTest {


    private String log(String message) {
        return "[" + new Date() + "]" + message;
    }
    @Test
    void testAsyncOp() throws Exception {
        int numberOfTasks = 10000;
        System.out.println("Initializing async tasks worker");
        TaskI worker = AsyncTasks.initialize(1); // only testing on a single worker, otherwise result have a high chance to not be in the order we want
        Stack<Integer> results = new Stack<>();
        for(int i = 0; i < numberOfTasks; i++) {
            System.out.println(log("sending task " + i));
            AtomicInteger atomicInteger = new AtomicInteger(i);
            worker.addTask(new AltarikRunnable() {
                @Override
                public void run() {
                    System.out.println(log(" task " + atomicInteger.get()));
                    results.push(atomicInteger.get());
                }
            });
        }
        worker.close(); // wait until all worker terminated
        Integer[] expected = new Integer[numberOfTasks];
        for(int i = 0; i < numberOfTasks; i++) {
            expected[i] = i;
        }
        assertArrayEquals(expected, results.toArray());

    }
}
