package networking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Supplier;

// A utility class to handle multiple tasks concurrently with a given number of threads.
public class Threads<R> {
    static int id = 1;
    int threads;
    ExecutorService executor; // Service that manages thread execution.
    List<Callable<R>> callables = new ArrayList<>(); // List of tasks waiting to be executed.

    public Threads(int threads, ExecutorService executorService) {
        this.threads = threads;
        this.executor = executorService;
    }

    public void submit_task(Supplier<R> function) {
        this.callables.add(new Task<>(id, function));
        id++;
    }

    public ArrayList<R> run_tasks() {
        try {
            return processFutures(this.executor.invokeAll(this.callables));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<R>();
        }
    }

    // Extract results from futures once tasks are executed.
    private ArrayList<R> processFutures(List<Future<R>> futures) {
        ArrayList<R> responses = new ArrayList<>();
        for (Future<R> future : futures) {
            try {
                responses.add(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responses;
    }

    // Represents a task with a unique identifier.
    static class Task<R> implements Callable<R> {
        int task_id;
        Supplier<R> function;

        public Task(int task_id, Supplier<R> function) {
            this.task_id = task_id;
            this.function = function;
        }

        @Override
        public R call() {
            return function.get();
        }
    }
}