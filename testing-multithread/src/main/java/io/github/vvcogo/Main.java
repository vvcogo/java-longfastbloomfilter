package io.github.vvcogo;

import io.github.vvcogo.longfastbloomfilter.framework.bitset.AtomicLongBitSet;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.BitSet;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigurationLoader;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.InvalidConfigurationException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger("testing-multithread");

    static {
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LogsFormat());
        LOGGER.addHandler(handler);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            LOGGER.severe(() -> "Usage ./testing-multithread <insert file> <config file> <num threads> <num executions>");
            System.exit(-1);
        }
        LOGGER.info(() -> "Reading inserts file...");
        List<String> inserts = FileReader.readFile(args[0]);
        LOGGER.info(() -> "Loading configuration...");
        BloomFilterConfiguration<String> config = loadConfiguration(args[1]);
        LOGGER.info(() -> "Configuration loaded: " + config.toString());

        int numThreads = Integer.parseInt(args[2]);
        int numExecutions = Integer.parseInt(args[3]);
        ExecutorService exec = Executors.newFixedThreadPool(numThreads);
        LOGGER.info(() -> "Created thread poool with " + numThreads + " threads!");

        for (int i = 0; i < numExecutions; i++) {
            testHashing(exec, inserts, numThreads, config);
            testSerializer(exec, inserts, numThreads, config);
            testBitset(exec, inserts, numThreads, config);
        }
    }

    private static void testSerializer(ExecutorService exec, List<String> inserts, int numThreads, BloomFilterConfiguration<String> config) throws Exception {
        Consumer<String> serializerConsumer = (s) -> config.getSerializer().serialize(s);
        List<Runnable> serializerTasks = createInvokeList(inserts, numThreads, serializerConsumer);
        executeTasks(exec, inserts, serializerTasks, "Finished serializing %s strings in %s ms");
    }

    private static void testHashing(ExecutorService exec, List<String> inserts, int numThreads, BloomFilterConfiguration<String> config) throws Exception {
        int k = config.getNumberOfHashFunctions();
        long m = config.getBitSetSize();
        List<byte[]> bytesList = inserts.stream().map(String::getBytes).toList();
        Consumer<byte[]> hashingConsumer = bytes -> config.getHashFunction().hash(bytes, k, m);
        List<Runnable> hashingTasks = createInvokeList(bytesList, numThreads, hashingConsumer);
        executeTasks(exec, inserts, hashingTasks, "Finished hashing %s byte arrays in %s ms");
    }

    private static void testBitset(ExecutorService exec, List<String> inserts, int numThreads, BloomFilterConfiguration<String> config) throws Exception {
        BitSet bitset = new AtomicLongBitSet(config.getBitSetSize());
        List<byte[]> bytesList = inserts.stream().map(String::getBytes).toList();
        List<long[]> indexes = new ArrayList<>();
        for (byte[] bytes : bytesList) {
            ByteBuffer buffer = getHashes(bytes, config);
            long[] array = new long[buffer.remaining() / Long.BYTES];
            int j = 0;
            while (buffer.remaining() > 7) {
                long index = buffer.getLong();
                array[j] = index;
                j++;
            }
            indexes.add(array);
        }
        Consumer<long[]> setConsumer = longArr -> {
            for(long index : longArr)
                bitset.set(index);
        };
        List<Runnable> setTasks = createInvokeList(indexes, numThreads, setConsumer);
        executeTasks(exec, inserts, setTasks, "Finished setting %s elements in %s ms");
    }

    //--------------------------------------------UTILS-------------------------------------------------------------------------
    private static ByteBuffer getHashes(byte[] bytes, BloomFilterConfiguration<String> config) {
        long size = config.getBitSetSize();
        int numbHashes = config.getNumberOfHashFunctions();
        return config.getHashFunction().hash(bytes, numbHashes, size);
    }

    private static <T> List<Runnable> createInvokeList(List<T> dataList, int numbThreads, Consumer<T> consumer) {
        List<Runnable> result = new ArrayList<>();
        int size = dataList.size();
        int amountPerThread = size / numbThreads;
        for (int i = 0; i < numbThreads; i++) {
            int threadIndex = i;
            result.add(() -> {
                for (int j = threadIndex * amountPerThread; j < amountPerThread * (threadIndex + 1); j++) {
                    consumer.accept(dataList.get(j));
                }
            });
        }
        return result;
    }

    private static void executeTasks(ExecutorService exec, List<String> elementList, List<Runnable> callableList,
                                     String message) throws InterruptedException, ExecutionException {
        List<Future<?>> tasks = new ArrayList<>(callableList.size());
        long start = System.currentTimeMillis();
        for (Runnable task : callableList) {
            tasks.add(exec.submit(task));
        }
        for (Future<?> task : tasks) {
            task.get();
        }
        long elapsed = System.currentTimeMillis() - start;
        LOGGER.info("");
        LOGGER.info(() -> String.format(message, elementList.size(), elapsed));
        throughput(elapsed, elementList.size());
        latency(elapsed, elementList.size());
        LOGGER.info("");
    }

    private static void throughput(long elapsed, long numElems) {
        double th = numElems / (elapsed / 1000.0);
        LOGGER.info(() -> " > Throughput: " + th + " e/s.");
    }

    private static void latency(long elapsed, long numElems) {
        double latency = ((double) elapsed) / numElems;
        LOGGER.info(() -> " > Latency: " + latency + " ms.");
    }

    private static BloomFilterConfiguration<String> loadConfiguration(String configFilePath) {
        try (FileInputStream inputStream = new FileInputStream(configFilePath)) {
            Properties props = new Properties();
            props.load(inputStream);
            BloomFilterConfigurationLoader<String> loader = BloomFilterConfigurationLoader.defaultLoader();
            return loader.loadConfig(props);
        } catch (FileNotFoundException e) {
            LOGGER.severe(() -> "Config file not found!");
            System.exit(1);
        } catch (IOException e) {
            LOGGER.severe(() -> "Failed to read config file!");
            System.exit(1);
        } catch (InvalidConfigurationException e) {
            LOGGER.severe(() -> "Configuration was invalid! " + e.getMessage());
            System.exit(1);
        }
        return null;
    }
}