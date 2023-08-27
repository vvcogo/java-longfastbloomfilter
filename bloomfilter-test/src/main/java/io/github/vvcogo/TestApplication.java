package io.github.vvcogo;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigurationLoader;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.InvalidConfigurationException;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.BloomFilterExtension;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.ExtensionLoadException;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.JavaExtensionLoader;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class TestApplication {
    private static final Logger ROOT_LOGGER = LoggerFactory.getLogger("");
    private static final Logger PATTERNLESS_LOGGER = LoggerFactory.getLogger("patternless");
    private static final File EXTENSIONS_DIRECTORY = new File("extensions/");

    public static void main(String[] args) throws InterruptedException {
        checkArguments(args);

        // for use with jConsole
        Thread.sleep(3000);

        JavaExtensionLoader extensionLoader = new JavaExtensionLoader();
        loadExtensions(extensionLoader);

        BloomFilterConfiguration<String> bc = loadConfiguration(args[2]);
        BloomFilter<String> bf = BloomFilterCreator.createBloomFilter(bc);

        ExecutorService exec = createThreadPool(args.length == 4 ? args[3] : null);

        List<String> listInsert = manageFile(args[0], "insert");
        List<String> listQuery = manageFile(args[1], "query");
        Set<String> setInsert = new HashSet<>(listInsert);

        executeInserts(exec, bf, listInsert);
        executeQueries(exec, bf, listQuery);
        checkFalsePositives(bf, setInsert, listQuery);

        exec.shutdown();
    }

    private static void checkArguments(String[] args) {
        if(args.length < 3 || args.length > 4) {
            String filePath = TestApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String jarFileName = filePath.substring(filePath.lastIndexOf("/"));
            ROOT_LOGGER.error(String.format("Usage: .%s <insert file> <query file> <config file> [number of threads]", jarFileName));
            System.exit(1);
        }
    }

    private static void executeInserts(ExecutorService exec, BloomFilter<String> bf, List<String> listInsert) throws InterruptedException {
        List<Callable<Object>> callInsert = new ArrayList<>();
        for(String elem : listInsert){
            callInsert.add(Executors.callable(() -> {
                bf.add(elem);
                ROOT_LOGGER.info("INSERTED: " + elem);
            } ));
        }
        long start = System.currentTimeMillis();
        exec.invokeAll(callInsert);
        long elapsed = System.currentTimeMillis() - start;
        PATTERNLESS_LOGGER.info("");
        ROOT_LOGGER.info(String.format("Finished inserting %s elements in %s ms%n", callInsert.size(), elapsed));
        throughput(elapsed, callInsert.size());
    }

    private static void executeQueries(ExecutorService exec, BloomFilter<String> bf, List<String> listQuery) throws InterruptedException {
        AtomicInteger failCount = new AtomicInteger();
        List<Callable<Object>> callQuery = new ArrayList<>();
        for (String elem : listQuery){
            callQuery.add(Executors.callable(() -> {
                boolean result = bf.mightContains(elem);
                ROOT_LOGGER.info(String.format("QUERY (%s): %s", elem, result));
                if (!result)
                    failCount.getAndIncrement();
            }));
        }
        long start = System.currentTimeMillis();
        exec.invokeAll(callQuery);
        long elapsed = System.currentTimeMillis() - start;
        PATTERNLESS_LOGGER.info("");
        ROOT_LOGGER.info(String.format("Finished querying %s elements in %s ms", callQuery.size(), elapsed));
        ROOT_LOGGER.info(String.format("%s/%s were false.", failCount.get(), callQuery.size()));
        throughput(elapsed, callQuery.size());
    }

    private static void checkFalsePositives(BloomFilter<String> bf, Set<String> setInsert, List<String> listQuery) {
        List<String> falsePositives = new ArrayList<>();
        for (String query : listQuery) {
            if (bf.mightContains(query) && !setInsert.contains(query)) {
                falsePositives.add(query);
            }
        }
        if (falsePositives.isEmpty()) {
            ROOT_LOGGER.info("No false positives were found!");
        } else {
            ROOT_LOGGER.info(String.format("False positives (%s): %s%n", falsePositives.size(), falsePositives));
        }
    }

    private static BloomFilterConfiguration<String> loadConfiguration(String configFilePath) {
        try (FileInputStream inputStream = new FileInputStream(configFilePath)) {
            Properties props = new Properties();
            props.load(inputStream);
            BloomFilterConfigurationLoader<String> loader = BloomFilterConfigurationLoader.defaultLoader();
            return loader.loadConfig(props);
        } catch (FileNotFoundException e) {
            ROOT_LOGGER.error("Config file not found!");
            System.exit(1);
        } catch (IOException e) {
            ROOT_LOGGER.error("Failed to read config file!");
            System.exit(1);
        } catch (InvalidConfigurationException e) {
            ROOT_LOGGER.error("Configuration was invalid! " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private static ExecutorService createThreadPool(String numbThreadsString) {
        int numberOfThreads = Runtime.getRuntime().availableProcessors() - 1;
        if (numbThreadsString != null) {
            int readNumber;
            try {
                readNumber = Integer.parseInt(numbThreadsString);
                if (readNumber <= 0) {
                    ROOT_LOGGER.error("Number of threads cannot be smaller than 1! Using: ");
                } else {
                    numberOfThreads = readNumber;
                }
            } catch (NumberFormatException e) {
                ROOT_LOGGER.error("Could not read number of threads!");
            }
        }
        ROOT_LOGGER.info("Creating Thread Pool with " + numberOfThreads + " threads.\n");
        return Executors.newFixedThreadPool(numberOfThreads);
    }

    private static List<String> manageFile(String path, String type){
        List<String> result = null;
        try {
            result = FileReader.readFile(path);
        } catch (FileNotFoundException e) {
            ROOT_LOGGER.error("Can not read " + type + " file.");
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }

    private static void loadExtensions(JavaExtensionLoader loader) {
        ROOT_LOGGER.info("Trying to load extensions from " + EXTENSIONS_DIRECTORY.getAbsolutePath());
        if (!EXTENSIONS_DIRECTORY.isDirectory())
            return;
        File[] files = EXTENSIONS_DIRECTORY.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                ROOT_LOGGER.info("LOADING: " + file.getName());
                try {
                    BloomFilterExtension extension = loader.loadExtension(file);
                    String name = extension.getProperties().getName();
                    String version = extension.getProperties().getVersion();
                    ROOT_LOGGER.info("\t> Extension loaded: " + name + " v" + version);
                    extension.onInit();
                } catch (ExtensionLoadException e) {
                    ROOT_LOGGER.error("Extension failed to load: " + e.getMessage());
                }
            }
        }
    }

    private static void throughput(long elapsed, int numElems) {
        double th = (double) numElems/elapsed;
        ROOT_LOGGER.info(String.format("Inserts throughput: %.2f e/ms.%n", th));
    }
}