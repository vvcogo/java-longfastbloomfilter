package io.github.vvcogo;

import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigurationLoader;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.InvalidConfigurationException;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.BloomFilterExtension;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.ExtensionLoadException;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.JavaExtensionLoader;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class TestApplicationV2 {

    private static final Logger LOGGER = Logger.getLogger("longfastbloomfilter");
    private static final File EXTENSIONS_DIRECTORY = new File("extensions/");
    private static final int NUMBER_GC_CALLS = 10;
    private static final int WARM_UP = 5;

    static {
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LogsFormat());
        LOGGER.addHandler(handler);
    }

    // input query config th
    public static void main(String[] args) throws Exception {
        if (args.length < 5) {
            System.err.println("Execute with 5 arguments! <insert file> <query file> <config file> <num threads> <num executions>");
            System.exit(-1);
        }
        String insertFile = args[0];
        String queryFile = args[1];
        String configFile = args[2];
        int numThreads = Integer.parseInt(args[3]);
        int numExecutions = Integer.parseInt(args[4]);

        JavaExtensionLoader extensionLoader = new JavaExtensionLoader();
        loadExtensions(extensionLoader);

        LOGGER.info(() -> "Loading config from file: " + configFile);
        BloomFilterConfiguration<String> configuration = loadConfiguration(configFile);
        LOGGER.info(configuration::toString);

        LOGGER.info(() -> "Reading inserts file...");
        List<String> inserts = FileReader.readFile(insertFile);
        LOGGER.info(() -> "Reading queries file...");
        List<String> queries = FileReader.readFile(queryFile);
        BloomFilter<String> bloomFilter = BloomFilterCreator.createBloomFilter(configuration);

        for (int i = 1; i <= WARM_UP; i++) {
            LOGGER.info("Executing warmup #" + i);
            callGC();
            long elapsed = executeTasks(inserts, numThreads, bloomFilter::add);
            elapsed += executeTasks(queries, numThreads, bloomFilter::mightContains);
            double elapsedMillis = elapsed / 1000000.0;
            LOGGER.info(String.format("Warmup %s finished in %s ms", i, elapsedMillis));
        }

        for (int i = 1; i <= numExecutions; i++) {
            LOGGER.info("Execution #" + i + ": ");
            // Inserts
            callGC();
            long elapsedNano = executeTasks(inserts, numThreads, bloomFilter::add);
            double elapsedMillis = elapsedNano / 1000000.0;
            LOGGER.info(() -> String.format("Finished inserting %s elements in %s ms", inserts.size(), elapsedMillis));
            throughput(elapsedMillis, inserts.size());
            latency(elapsedMillis, inserts.size());
            // Queries
            callGC();
            long elapsedNanoQueries = executeTasks(queries, numThreads, bloomFilter::mightContains);
            double elapsedMillisQueries = elapsedNanoQueries / 1000000.0;
            LOGGER.info(() -> String.format("Finished querying %s elements in %s ms", queries.size(), elapsedMillisQueries));
            throughput(elapsedMillisQueries, queries.size());
            latency(elapsedMillisQueries, queries.size());
        }
    }

    public static void callGC() {
        for (int i = 0; i < NUMBER_GC_CALLS; i++) {
            System.gc();
            System.runFinalization();
        }
    }

    private static <T> long executeTasks(List<T> data, int numThreads, Consumer<T> consumer) throws InterruptedException {
        final int amountPerThread = data.size() / numThreads;
        List<Runnable> tasks = new ArrayList<>(numThreads);
        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            tasks.add(() -> {
                for (int j = threadIndex * amountPerThread; j < (threadIndex + 1) * amountPerThread; j++) {
                    consumer.accept(data.get(j));
                }
            });
        }
        List<Thread> workers = new ArrayList<>(numThreads);
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
        return System.nanoTime() - start;
    }

    private static void loadExtensions(JavaExtensionLoader loader) {
        LOGGER.info(() -> "Trying to load extensions from " + EXTENSIONS_DIRECTORY.getAbsolutePath());
        if (!EXTENSIONS_DIRECTORY.isDirectory())
            return;
        File[] files = EXTENSIONS_DIRECTORY.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                LOGGER.info(() -> "LOADING: " + file.getName());
                try {
                    BloomFilterExtension extension = loader.loadExtension(file);
                    String name = extension.getProperties().getName();
                    String version = extension.getProperties().getVersion();
                    LOGGER.info(() -> "\t> Extension loaded: " + name + " v" + version);
                    extension.onInit();
                } catch (ExtensionLoadException e) {
                    LOGGER.severe(() -> "Extension failed to load: " + e.getMessage());
                }
            }
        }
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

    private static void throughput(double elapsed, long numElems) {
        double th = numElems / (elapsed / 1000.0);
        LOGGER.info(() -> " > Throughput: " + th + " e/s.");
    }

    private static void latency(double elapsed, long numElems) {
        double latency = elapsed / numElems;
        LOGGER.info(() -> " > Latency: " + latency + " ms.");
    }
}
