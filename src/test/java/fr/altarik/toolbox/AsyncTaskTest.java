package fr.altarik.toolbox;

import fr.altarik.toolbox.asynctasks.AsyncTasks;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AsyncTaskTest {

    @Test
    public void testAsyncOp() {
        System.out.println("Initializing async tasks worker");
        AsyncTasks.initialize();
        Stack<Integer> results = new Stack<>();
        System.out.println("[" + new Date() + "] sending task 1");
        AsyncTasks.addTask(() -> {
            System.out.println("[" + new Date() + "] task 1");
            results.push(1);
        });
        System.out.println("[" + new Date() + "] sending task 2");
        AsyncTasks.addTask(() -> {
            System.out.println("[" + new Date() + "] task 2");
            results.push(2);
        });
        System.out.println("[" + new Date() + "] sending task 3");
        AsyncTasks.addTask(() -> {
            System.out.println("[" + new Date() + "] task 3");
            results.push(3);
        });
        System.out.println("[" + new Date() + "] sending task 4");
        AsyncTasks.addTask(() -> {
            System.out.println("[" + new Date() + "] task 4");
            results.push(4);
        });
        System.out.println("[" + new Date() + "] sending task 5");
        AsyncTasks.addTask(() -> {
            System.out.println("[" + new Date() + "] task 5");
            results.push(5);
        });
        while(AsyncTasks.numberOfWaitingTask() != 0) {
            try {
                synchronized (this) {
                    wait(20); // wait till last task finish
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, results.toArray());

    }
}
