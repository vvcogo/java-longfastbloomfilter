package io.github.vvcogo.longfastbloomfilter.framework;

import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterFactoryManager;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashFunction;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class BloomFilterConfigurationLoader<T> {

    private long bitSetSize; // n, p
    private long expectedNumberOfElements; // m, p
    private int numberOfHashFunctions; // m, n
    private double falsePositiveProbability; // obrigatorio
    private HashingAlgorithm hashingAlgorithm;
    private Serializer<? super T> serializer;
    private String typeBf; // obrigatorio

    private BloomFilterConfigurationLoader(Properties properties) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        this.bitSetSize = Long.parseLong(properties.getProperty("bitset-size", "-1"));
        this.expectedNumberOfElements = Long.parseLong(properties.getProperty("expected-elements", "-1"));
        this.numberOfHashFunctions = Integer.parseInt(properties.getProperty("number-hash-functions", "-1"));
        this.falsePositiveProbability = Double.parseDouble(properties.getProperty("false-positive-probability", "-1"));
        HashFunction hashFunction = HashFunction.valueOf(properties.getProperty("hash-function", "MURMUR_KIRSCH_MITZENMACHER"));
        this.hashingAlgorithm = hashFunction.getHashingAlgorithm();
        Class<?> serializerClass = Class.forName(properties.getProperty("serializer"));
        this.serializer = (Serializer<? super T>) serializerClass.getDeclaredConstructor().newInstance();
        this.typeBf = properties.getProperty("bf-type", "longfastbloomfilter");
    }

    public static <T> BloomFilterConfigurationLoader<T> load(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);
        try {
            return new BloomFilterConfigurationLoader<>(props);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            // FIXME: exception
            throw new RuntimeException("Invalid configuration");
        }
    }

    public BloomFilter<T> createBloomFilter() { //FIXME: dar check
        BloomFilterConfiguration<T> config = new BloomFilterConfiguration<>(this.bitSetSize, this.expectedNumberOfElements,
                this.numberOfHashFunctions, this.falsePositiveProbability, this.hashingAlgorithm, this.serializer);
        return BloomFilterFactoryManager.getFactory(this.typeBf).create(config);
    }

}
