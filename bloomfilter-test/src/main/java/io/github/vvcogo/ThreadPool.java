package io.github.vvcogo;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.StandardLongBloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashAlgorithms;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPool {

    private static ExecutorService exec;

    private static final long BIT_SET = Integer.MAX_VALUE;
    private static final long EXP = 10_000;
    private static final int HASH_NUM = 5;
    private static final double POS_RATE = 0.01;
    private static final HashingAlgorithm HASH_FUNC = HashAlgorithms.MURMUR;
    private static Serializer<String> sr = (elem) -> elem.getBytes();

    private static BloomFilterConfiguration<String> bc = new BloomFilterConfiguration<>(BIT_SET, EXP, HASH_NUM, POS_RATE, HASH_FUNC, sr);
    private static BloomFilter<String> bf = new StandardLongBloomFilter<>(bc);

    public static void main(String[] args) throws InterruptedException {

        if(args.length != 2) {
            System.err.println("Invalid number of parameters");
            System.exit(1);
        }

        String fileInsertPath = args[0];
        String fileQueryPath = args[1];
        List<String> listInsert = manageFile(fileInsertPath, "insert");
        List<String> listQuery = manageFile(fileQueryPath, "query");

        //threads---------------
        exec = Executors.newFixedThreadPool(5);

        List<Callable<Object>> callInsert = new ArrayList<>();
        for(String elem : listInsert){
            callInsert.add(Executors.callable(() -> bf.add(elem)));
        }
        exec.invokeAll(callInsert);

        List<Callable<Boolean>> callQuery = new ArrayList<>();
        for(String elem : listQuery){
            callQuery.add(() -> bf.mightContains(elem));
        }

        List<Future<Boolean>> futureQuery = exec.invokeAll(callQuery);
        for (Future<Boolean> elem : futureQuery)
            System.out.println(elem);

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