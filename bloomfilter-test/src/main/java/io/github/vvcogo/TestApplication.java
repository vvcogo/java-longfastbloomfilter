package io.github.vvcogo;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigurationLoader;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.InvalidConfigurationException;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.BloomFilterExtension;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.ExtensionLoadException;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.JavaExtensionLoader;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterCreator;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public final class TestApplication {

    private static final Logger LOGGER = Logger.getLogger("longfastbloomfilter");
    private static final File EXTENSIONS_DIRECTORY = new File("extensions/");
    private static final int WARMUP_AMOUNT = 50;

    static {
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LogsFormat());
        LOGGER.addHandler(handler);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        checkArguments(args);

        JavaExtensionLoader extensionLoader = new JavaExtensionLoader();
        loadExtensions(extensionLoader);

        LOGGER.info(() -> "Loading config from file: " + args[2]);
        BloomFilterConfiguration<String> bc = loadConfiguration(args[2]);
        LOGGER.info(bc::toString);
        BloomFilter<String> bf = BloomFilterCreator.createBloomFilter(bc);
        executeWarmUp(bf);
        LOGGER.info(() -> "Warmed up bloomfilter with " + WARMUP_AMOUNT + " inserts."   );

        int numbThreads = getNumberOfThreads(args.length == 4 ? args[3] : null);
        ExecutorService exec = Executors.newFixedThreadPool(numbThreads);

        LOGGER.info("Reading insert file...");
        List<String> listInsert = manageFile(args[0], "insert");
        LOGGER.info("Reading query file...");
        List<String> listQuery = manageFile(args[1], "query");
        Set<String> setInsert = new HashSet<>(listInsert);

        List<Runnable> callInsert = createInvokeList(listInsert, numbThreads, bf::add);
        List<Runnable> callQuery = createInvokeList(listQuery, numbThreads, bf::mightContains);

        LOGGER.info("Executing inserts...");
        executeTasks(exec, listInsert, callInsert, "Finished inserting %s elements in %s ms");
        LOGGER.info("Executing queries...");
        executeTasks(exec, listQuery, callQuery, "Finished querying %s elements in %s ms");
        checkFalsePositives(bf, setInsert, listQuery);

        LOGGER.info(() -> "Shuting down...");
        exec.shutdown();
    }

    private static void checkArguments(String[] args) {
        if(args.length < 3 || args.length > 4) {
            String filePath = TestApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String jarFileName = filePath.substring(filePath.lastIndexOf("/"));
            LOGGER.severe(() -> "Usage: ." + jarFileName + " <insert file> <query file> <config file> [number of threads]");
            System.exit(1);
        }
    }

    private static void executeWarmUp(BloomFilter<String> bloomFilter) {
        for (int i = 0; i < WARMUP_AMOUNT ; i += 1) {
            bloomFilter.add((i * 64) + "");
        }
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

    private static void checkFalsePositives(BloomFilter<String> bf, Set<String> setInsert, List<String> listQuery) {
        List<String> falsePositives = new ArrayList<>();
        for (String query : listQuery) {
            if (bf.mightContains(query) && !setInsert.contains(query)) {
                falsePositives.add(query);
            }
        }
        if (falsePositives.isEmpty()) {
            LOGGER.info(() ->"No false positives were found!");
        } else {
            LOGGER.info(() -> "False positives: " + falsePositives.size() + "/" + listQuery.size());
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

    private static int getNumberOfThreads(String numbThreadsString) {
        int numberOfThreads = Runtime.getRuntime().availableProcessors() - 1;
        if (numbThreadsString != null) {
            int readNumber;
            try {
                readNumber = Integer.parseInt(numbThreadsString);
                if (readNumber <= 0) {
                    LOGGER.warning(() -> "Number of threads cannot be smaller than 1! Using: ");
                } else {
                    numberOfThreads = readNumber;
                }
            } catch (NumberFormatException e) {
                LOGGER.severe(() -> "Could not read number of threads!");
            }
        }
        LOGGER.info("Created Thread Pool with " + numberOfThreads + " threads.\n");
        return numberOfThreads;
    }

    private static List<String> manageFile(String path, String type){
        List<String> result = null;
        try {
            result = FileReader.readFile(path);
        } catch (FileNotFoundException e) {
            LOGGER.severe(() -> "Can not read " + type + " file.");
            LOGGER.severe(e::getMessage);
            System.exit(1);
        }
        return result;
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

    private static void throughput(long elapsed, long numElems) {
        double th = numElems / (elapsed / 1000.0);
        LOGGER.info(() -> " > Throughput: " + th + " e/s.");
    }

    private static void latency(long elapsed, long numElems) {
        double latency = ((double) elapsed) / numElems;
        LOGGER.info(() -> " > Latency: " + latency + " ms.");
    }

    private static List<Runnable> createInvokeList(List<String> dataList, int numbThreads, Consumer<String> consumer) {
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
}