package io.github.vvcogo;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterConfigurationLoader;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.StandardLongBloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashFunction;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TestApplication {

    private static final long BIT_SET = Integer.MAX_VALUE;
    private static final long EXP = 10_000;
    private static final int HASH_NUM = 5;
    private static final double POS_RATE = 0.01;
    private static final HashingAlgorithm HASH_FUNC = HashFunction.MURMUR.getHashingAlgorithm();
    private static Serializer<String> sr = (elem) -> elem.getBytes();

    private static BloomFilterConfiguration<String> bc = new BloomFilterConfiguration<>(BIT_SET, EXP, HASH_NUM, POS_RATE, HASH_FUNC, sr);
    private static BloomFilter<String> bf = new StandardLongBloomFilter<>(bc);

    public static void main(String[] args) throws InterruptedException {

//        System.out.println(System.out);

        if(args.length < 2 || args.length > 4) {
            String filePath = TestApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String jarFileName = filePath.substring(filePath.lastIndexOf("/"));
            System.err.println(String.format("Usage: .%s <insert file> <query file> <config file> <number of threads>", jarFileName));
            System.exit(1);
        }

        String fileInsertPath = args[0];
        String fileQueryPath = args[1];
        List<String> listInsert = manageFile(fileInsertPath, "insert");
        List<String> listQuery = manageFile(fileQueryPath, "query");

        if (args.length == 3) {
            String configFilePath = args[2];
            try (FileInputStream inputStream = new FileInputStream(new File(configFilePath))) {
                bf = BloomFilterConfigurationLoader.<String>load(inputStream).createBloomFilter();
            } catch (FileNotFoundException e) {
                System.err.println("Config file not found!");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Failed to read config file!");
                System.exit(1);
            }
        }

        int numberOfThreads = Runtime.getRuntime().availableProcessors() - 1;
        if (args.length == 4) {
            int readNumber;
            try {
                readNumber = Integer.parseInt(args[3]);
                if (readNumber <= 0) {
                    System.err.println("Number of threads cannot be smaller than 1! Using: ");
                } else {
                    numberOfThreads = readNumber;
                }
            } catch (NumberFormatException e) {
                System.err.println("Could not read number of threads!");
            }
        }

        //threads---------------
        System.out.println("\nCreating Thread Pool with " + numberOfThreads + " threads.\n");
        ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);

        List<Callable<Object>> callInsert = new ArrayList<>();
        for(String elem : listInsert){
            callInsert.add(Executors.callable(() -> {
                bf.add(elem);
                System.out.printf("[Thread: %s] INSERTED: %s%n", Thread.currentThread().getName(), elem);
            } ));
        }
        long start = System.currentTimeMillis();
        exec.invokeAll(callInsert);
        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("\nFinished inserting %s elements in %s ms\n", callInsert.size(), elapsed);

        AtomicInteger failCount = new AtomicInteger();
        List<Callable<Object>> callQuery = new ArrayList<>();
        for (String elem : listQuery){
            callQuery.add(Executors.callable(() -> {
                boolean result = bf.mightContains(elem);
                System.out.println(String.format("[Thread: %s] QUERY (%s): %s", Thread.currentThread().getName(), elem, result));
                if (!result)
                    failCount.getAndIncrement();
            }));
        }
        System.out.println();

        start = System.currentTimeMillis();
        exec.invokeAll(callQuery);
        elapsed = System.currentTimeMillis() - start;
        System.out.println(String.format("\nFinished querying %s elements in %s ms", callQuery.size(), elapsed));
        System.out.println(String.format("%s/%s were false.", failCount.get(), callQuery.size()));

        List<String> falsePositives = new ArrayList<>();
        for (String query : listQuery) {
            if (bf.mightContains(query) && !listInsert.contains(query)) {
                falsePositives.add(query);
            }
        }
        System.out.printf("False positives (%s): %s\n\n", falsePositives.size(), falsePositives);
        exec.shutdown();
    }

    public static List<String> manageFile(String path, String type){
        List<String> result = null;
        try {
            result = FileReader.readFile(path);
        } catch (FileNotFoundException e) {
            System.err.println("Can not read " + type + " file.");
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }
}