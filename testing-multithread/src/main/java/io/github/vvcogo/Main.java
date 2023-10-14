package io.github.vvcogo;

import com.google.common.hash.GuavaBitsetAdapter;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.AtomicLongBitSet;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.BitSet;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.LongBitSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger("testing-multithread");
    private static final int AMOUNT_OF_GC_CALLS = 20;
    private static final int BITSET_SIZE = 7_000_000;
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
        if (args.length != 3) {
            LOGGER.severe(() -> "Usage ./testing-multithread <num threads> <num executions> <bf type>");
            System.exit(-1);
        }
        int numThreads = Integer.parseInt(args[0]);
        int numExecutions = Integer.parseInt(args[1]);
        String bfType = args[2];
        BitSet bitSet;
        if (bfType.equals("guava")) {
            bitSet = new GuavaBitsetAdapter(BITSET_SIZE);
        } else if (bfType.equals("long")) {
            bitSet = new LongBitSet(BITSET_SIZE);
        } else if (bfType.equals("atomic")) {
            bitSet = new AtomicLongBitSet(BITSET_SIZE);
        } else {
            throw new Exception();
        }
        LOGGER.info(() -> "Created thread poool with " + numThreads + " threads!");
        for (int i = 0; i < WARMUP_COUNT; i++) {
            LOGGER.info("Running warmup #" + (i + 1));
            double elapsed = runBenchmark(numThreads, bitSet);
            LOGGER.info(String.format("Warmup #%s finished in %s ms", i + 1, elapsed));
        }
        for (int i = 0; i < numExecutions; i++) {
            callGC();
            double elapsed = runBenchmark(numThreads, bitSet);
            LOGGER.info(() -> String.format("[%s] Executed %s (%s sets executed) inserts in %s ms", bfType, BITSET_SIZE, BITSET_SIZE * NUM_HASH_FUNCTIONS, elapsed));
        }
    }

    private static double runBenchmark(int numThreads, BitSet bitSet) throws Exception {
//        BitSet bitSet;
//        if (bfType.equals("guava")) {
//            bitSet = new GuavaBitsetAdapter(BITSET_SIZE);
//        } else if (bfType.equals("long")) {
//            bitSet = new LongBitSet(BITSET_SIZE);
//        } else if (bfType.equals("atomic")) {
//            bitSet = new AtomicLongBitSet(BITSET_SIZE);
//        } else {
//            throw new Exception();
//        }
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
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            tasks.add(() -> {
                for (int j = threadIndex * amountPerThread; j < (threadIndex + 1) * amountPerThread; j++) {
                    for (int k = 0; k < indexes[j].length; k++) {
                        bitSet.set(indexes[j][k]);
                    }
                }
            });
        }
        List<Thread> workers = new ArrayList<>();
        for (Runnable task : tasks) {
            workers.add(new Thread(task));
        }

        long start = System.nanoTime();
        for (Thread worker : workers) {
            worker.start();
        }
        for (Thread worker : workers) {
            worker.join();
        }
        long elapsed = System.nanoTime() - start;
        return elapsed / 1000000.0;
    }
}