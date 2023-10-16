package io.github.vvcogo;

import com.google.common.hash.GuavaBitsetAdapter;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.AtomicLongBitSet;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.BitSet;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.LongBitSet;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class Main {

    private static final File EXTENSIONS_DIRECTORY = new File("extensions/");
    private static final Logger LOGGER = Logger.getLogger("orestes-testing");

    public static void main(String[] args) {
        JavaExtensionLoader extensionLoader = new JavaExtensionLoader();
        loadExtensions(extensionLoader);


        BloomFilterConfiguration<String> config = loadConfiguration("config.properties");
        BloomFilter<String> bf = BloomFilterCreator.createBloomFilter(config);

//        bf.mightContains("BRUH");

        for (int i = 0; i < 1000; i++) {
            bf.add(i + "");
        }
//
        for (int i = 0; i < 1000; i++) {
            if (!bf.mightContains(i + "")) {
                LOGGER.severe("BAD " + i);
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
}