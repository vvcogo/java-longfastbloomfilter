package io.github.vvcogo;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterConfigurationLoader;
import io.github.vvcogo.longfastbloomfilter.framework.InvalidConfigurationException;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.BloomFilterExtension;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.ExtensionProperties;
import io.github.vvcogo.longfastbloomfilter.framework.extensions.JavaExtensionLoader;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TestApplication {

    private static final Logger ROOT_LOGGER = LoggerFactory.getLogger("");
    private static final Logger PATTERNLESS_LOGGER = LoggerFactory.getLogger("patternless");
    private static final File EXTENSIONS_DIRECTORY = new File("extensions/");

    public static void main(String[] args) throws InterruptedException {

        if(args.length < 2 || args.length > 4) {
            String filePath = TestApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String jarFileName = filePath.substring(filePath.lastIndexOf("/"));
            ROOT_LOGGER.error(String.format("Usage: .%s <insert file> <query file> <config file> <number of threads>", jarFileName));
            System.exit(1);
        }

        JavaExtensionLoader extensionLoader = new JavaExtensionLoader();
        loadExtensions(extensionLoader);

        Set<String> loaded = extensionLoader.getLoadedExtensions();
        if (!loaded.isEmpty()) {
            ROOT_LOGGER.info("LOADED EXTENSIONS:");
            for (String extension : loaded) {
                ExtensionProperties properties = extensionLoader.getLoadedExtensionsData().get(extension).getProperties();
                ROOT_LOGGER.info(String.format("\t> %s v%s", properties.getName(), properties.getVersion()));
            }
        }

        String fileInsertPath = args[0];
        String fileQueryPath = args[1];
        List<String> listInsert = manageFile(fileInsertPath, "insert");
        List<String> listQuery = manageFile(fileQueryPath, "query");

        BloomFilterConfiguration<String> bc = null;
        if (args.length >= 3) {
            String configFilePath = args[2];
            try (FileInputStream inputStream = new FileInputStream(configFilePath)) {
                Properties props = new Properties();
                props.load(inputStream);
                bc = new BloomFilterConfigurationLoader<String>(props).getConfiguration();
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
        }

        BloomFilter<String> bf = BloomFilterCreator.createBloomFilter(bc);

        int numberOfThreads = Runtime.getRuntime().availableProcessors() - 1;
        if (args.length == 4) {
            int readNumber;
            try {
                readNumber = Integer.parseInt(args[3]);
                if (readNumber <= 0) {
                    ROOT_LOGGER.error("Number of threads cannot be smaller than 1! Using: ");
                } else {
                    numberOfThreads = readNumber;
                }
            } catch (NumberFormatException e) {
                ROOT_LOGGER.error("Could not read number of threads!");
            }
        }

        //threads---------------
        ROOT_LOGGER.info("Creating Thread Pool with " + numberOfThreads + " threads.\n");
        ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);

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

        start = System.currentTimeMillis();
        exec.invokeAll(callQuery);
        elapsed = System.currentTimeMillis() - start;

        PATTERNLESS_LOGGER.info("");
        ROOT_LOGGER.info(String.format("Finished querying %s elements in %s ms", callQuery.size(), elapsed));
        ROOT_LOGGER.info(String.format("%s/%s were false.", failCount.get(), callQuery.size()));

        List<String> falsePositives = new ArrayList<>();
        for (String query : listQuery) {
            if (bf.mightContains(query) && !listInsert.contains(query)) {
                falsePositives.add(query);
            }
        }
        if (falsePositives.isEmpty()) {
            ROOT_LOGGER.info("No false positives were found!");
        } else {
            ROOT_LOGGER.info(String.format("False positives (%s): %s%n", falsePositives.size(), falsePositives));
        }
        exec.shutdown();
    }

    public static List<String> manageFile(String path, String type){
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
                BloomFilterExtension extension = loader.loadExtension(file);
                extension.onInit();
            }
        }
    }
}