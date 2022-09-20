package fr.altarik.toolbox;

import fr.altarik.toolbox.asynctasks.AsyncTasks;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AsyncTaskTest {


    private String log(String message) {
        return "[" + new Date() + "]" + message;
    }
    @Test
    public void testAsyncOp() throws InterruptedException {
        int numberOfTasks = 10000;
        System.out.println("Initializing async tasks worker");
        AsyncTasks.initialize();
        Stack<Integer> results = new Stack<>();
        for(int i = 0; i < numberOfTasks; i++) {
            System.out.println("[" + new Date() + "] sending task " + i);
            AtomicInteger atomicInteger = new AtomicInteger(i);
            AsyncTasks.addTask(() -> {
                System.out.println(log(" task " + atomicInteger.get()));
                results.push(atomicInteger.get());
            });
        }
        while(AsyncTasks.numberOfWaitingTask() != 0) {
            try {
                synchronized (this) {
                    wait(20); // wait till last task finish
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Integer[] expected = new Integer[numberOfTasks];
        for(int i = 0; i < numberOfTasks; i++) {
            expected[i] = i;
        }
        assertArrayEquals(expected, results.toArray());

    }
}
