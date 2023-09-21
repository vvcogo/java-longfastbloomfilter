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
import java.util.function.Consumer;

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

        ROOT_LOGGER.info("Loading config from file: {}", args[2]);
        BloomFilterConfiguration<String> bc = loadConfiguration(args[2]);
        ROOT_LOGGER.info("{}", bc);
        BloomFilter<String> bf = BloomFilterCreator.createBloomFilter(bc);

        int numbThreads = getNumberOfThreads(args.length == 4 ? args[3] : null);
        ExecutorService exec = Executors.newFixedThreadPool(numbThreads);

        List<String> listInsert = manageFile(args[0], "insert");
        List<String> listQuery = manageFile(args[1], "query");
        Set<String> setInsert = new HashSet<>(listInsert);

        executeInserts(exec, bf, listInsert, numbThreads);
        executeQueries(exec, bf, listQuery, numbThreads);
        checkFalsePositives(bf, setInsert, listQuery);

        exec.shutdown();
    }

    private static void checkArguments(String[] args) {
        if(args.length < 3 || args.length > 4) {
            String filePath = TestApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String jarFileName = filePath.substring(filePath.lastIndexOf("/"));
            ROOT_LOGGER.error("Usage: .{} <insert file> <query file> <config file> [number of threads]", jarFileName);
            System.exit(1);
        }
    }

    private static void executeInserts(ExecutorService exec, BloomFilter<String> bf, List<String> listInsert, int numbThreads) throws InterruptedException {
        List<Callable<Object>> callInsert = createInvokeList(listInsert, numbThreads, bf::add);
        long start = System.currentTimeMillis();
        exec.invokeAll(callInsert);
        long elapsed = System.currentTimeMillis() - start;
        PATTERNLESS_LOGGER.info("");
        ROOT_LOGGER.info("Finished inserting {} elements in {} ms", listInsert.size(), elapsed);
        throughput(elapsed, listInsert.size());
        latency(elapsed, listInsert.size());
        PATTERNLESS_LOGGER.info("");
    }

    private static void executeQueries(ExecutorService exec, BloomFilter<String> bf, List<String> listQuery, int numbThreads) throws InterruptedException {
        List<Callable<Object>> callQuery = createInvokeList(listQuery, numbThreads, bf::mightContains);
        long start = System.currentTimeMillis();
        exec.invokeAll(callQuery);
        long elapsed = System.currentTimeMillis() - start;
        PATTERNLESS_LOGGER.info("");
        ROOT_LOGGER.info("Finished querying {} elements in {} ms", listQuery.size(), elapsed);
        throughput(elapsed, listQuery.size());
        latency(elapsed, listQuery.size());
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
            ROOT_LOGGER.info("False positives: {}/{}", falsePositives.size(), listQuery.size());
            ROOT_LOGGER.debug("{}", falsePositives);
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
            ROOT_LOGGER.error("Configuration was invalid! {}", e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private static int getNumberOfThreads(String numbThreadsString) {
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
        ROOT_LOGGER.info("Creating Thread Pool with {} threads.\n", numberOfThreads);
        return numberOfThreads;
    }

    private static List<String> manageFile(String path, String type){
        List<String> result = null;
        try {
            result = FileReader.readFile(path);
        } catch (FileNotFoundException e) {
            ROOT_LOGGER.error("Can not read {} file.", type);
            ROOT_LOGGER.error("{}", e.getMessage());
            System.exit(1);
        }
        return result;
    }

    private static void loadExtensions(JavaExtensionLoader loader) {
        ROOT_LOGGER.info("Trying to load extensions from {}", EXTENSIONS_DIRECTORY.getAbsolutePath());
        if (!EXTENSIONS_DIRECTORY.isDirectory())
            return;
        File[] files = EXTENSIONS_DIRECTORY.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                ROOT_LOGGER.info("LOADING: {}", file.getName());
                try {
                    BloomFilterExtension extension = loader.loadExtension(file);
                    String name = extension.getProperties().getName();
                    String version = extension.getProperties().getVersion();
                    ROOT_LOGGER.info("\t> Extension loaded: {} v{}", name, version);
                    extension.onInit();
                } catch (ExtensionLoadException e) {
                    ROOT_LOGGER.error("Extension failed to load: {}", e.getMessage());
                }
            }
        }
    }

    private static void throughput(long elapsed, long numElems) {
        double th = numElems / (elapsed / 1000.0);
        ROOT_LOGGER.info(" > Throughput: {} e/s.", th);
    }

    private static void latency(long elapsed, long numElems) {
        double latency = ((double) elapsed) / numElems;
        ROOT_LOGGER.info(" > Latency: {} ms.", latency);
    }

    private static List<Callable<Object>> createInvokeList(List<String> dataList, int numbThreads, Consumer<String> consumer) {
        List<Callable<Object>> result = new ArrayList<>();
        int size = dataList.size();
        int amountPerThread = size / numbThreads;
        for (int i = 0; i < numbThreads; i++) {
            int threadIndex = i;
            result.add(Executors.callable(() -> {
               for (int j = threadIndex * amountPerThread; j < amountPerThread * (threadIndex + 1); j++) {
                   consumer.accept(dataList.get(j));
               }
            }));
        }
        return result;
    }
}