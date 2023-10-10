package io.github.vvcogo;

import io.github.vvcogo.longfastbloomfilter.framework.bitset.BitSet;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.LongBitSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger("testing-multithread");
    private static final int AMOUNT_OF_GC_CALLS = 20;
    private static final int BITSET_SIZE = 13_000_000;
    private static final int NUM_HASH_FUNCTIONS = 19;
    private static final int WARMUP_COUNT = 5;

    static {
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LogsFormat());
        LOGGER.addHandler(handler);
    }

    public static void callGC() {
        for (int i = 0; i < AMOUNT_OF_GC_CALLS; i++) {
            System.gc();
            System.runFinalization();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            LOGGER.severe(() -> "Usage ./testing-multithread <num threads> <num executions>");
            System.exit(-1);
        }
        int numThreads = Integer.parseInt(args[0]);
        int numExecutions = Integer.parseInt(args[1]);
        ExecutorService exec = Executors.newFixedThreadPool(numThreads);
        LOGGER.info(() -> "Created thread poool with " + numThreads + " threads!");
        for (int i = 0; i < WARMUP_COUNT; i++) {
            LOGGER.info("Running warmup #" + (i + 1));
            double elapsed = runBenchmark(exec, numThreads);
            LOGGER.info(String.format("Warmup #%s finished in %s ms", i + 1, elapsed));
        }
        for (int i = 0; i < numExecutions; i++) {
            callGC();
            double elapsed = runBenchmark(exec, numThreads);
            LOGGER.info(String.format("Executed %s (%s sets executed) inserts in %s ms", BITSET_SIZE, BITSET_SIZE * NUM_HASH_FUNCTIONS, elapsed));
        }
        exec.shutdown();
    }

    private static double runBenchmark(ExecutorService exec, int numThreads) throws Exception {
        BitSet bitSet = new LongBitSet(BITSET_SIZE);
        long[][] indexes = new long[BITSET_SIZE][];
        long index = 0;
        for (int i = 0; i < BITSET_SIZE; i++) {
            indexes[i] = new long[NUM_HASH_FUNCTIONS];
            for (int j = 0; j < NUM_HASH_FUNCTIONS; j++) {
                indexes[i][j] = (index & Long.MAX_VALUE) % BITSET_SIZE;
                index += 64;
            }
        }
        int amountPerThread = BITSET_SIZE / numThreads;
        List<Callable<Object>> tasks = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            tasks.add(Executors.callable(() -> {
                for (int j = threadIndex * amountPerThread; j < (threadIndex + 1) * amountPerThread; j++) {
                    for (int k = 0; k < indexes[j].length; k++) {
                        bitSet.set(indexes[j][k]);
                    }
                }
            }));
        }
        List<WorkerThread> workers = new ArrayList<>();
        for (Callable<Object> task : tasks) {
            workers.add(new WorkerThread(task));
        }

        long start = System.nanoTime();
        for (WorkerThread worker : workers) {
            worker.start();
        }
        for (WorkerThread worker : workers) {
            worker.join();
        }
        long elapsed = System.nanoTime() - start;
        return elapsed / 1000000.0;
    }

    private static class WorkerThread extends Thread {

        private final Callable<?> task;

        public WorkerThread(Callable<?> task) {
            this.task = task;
        }

        @Override
        public void run() {
            try {
                this.task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}