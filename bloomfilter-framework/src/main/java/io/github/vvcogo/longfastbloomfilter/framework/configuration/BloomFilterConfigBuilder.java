package io.github.vvcogo.longfastbloomfilter.framework.configuration;

import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.SerializerFactory;

import java.lang.reflect.InvocationTargetException;

public class BloomFilterConfigBuilder<T> {

    private HashingAlgorithm hashingAlgorithm;
    private String serializerClass;
    private String bloomFilterType;
    private double falsePositiveProbability;
    private long expectedNumberOfElements;
    private long bitSetSize;
    private int numberOfHashFunctions;

    public BloomFilterConfigBuilder<T> setBitSetSize(long bitSetSize){
        this.bitSetSize = bitSetSize;
        return this;
    }

    public BloomFilterConfigBuilder<T> setExpectedNumberOfElements(long expectedNumberOfElements){
        this.expectedNumberOfElements = expectedNumberOfElements;
        return this;
    }

    public BloomFilterConfigBuilder<T> setNumberOfHashFunctions(int numberOfHashFunctions){
        this.numberOfHashFunctions = numberOfHashFunctions;
        return this;
    }

    public BloomFilterConfigBuilder<T> setFalsePositiveProbability(double falsePositiveProbability){
        this.falsePositiveProbability = falsePositiveProbability;
        return this;
    }

    public BloomFilterConfigBuilder<T> setBloomFilterType(String bloomFilterType){
        this.bloomFilterType = bloomFilterType;
        return this;
    }

    public BloomFilterConfigBuilder<T> setHashingAlgorithm(HashingAlgorithm hashingAlgorithm){
        this.hashingAlgorithm = hashingAlgorithm;
        return this;
    }

    public BloomFilterConfigBuilder<T> setSerializer(String serializerClass){
        this.serializerClass = serializerClass;
        return this;
    }

    public BloomFilterConfiguration<T> build() throws InvalidConfigurationException {
        try {
            Serializer<? super T> serializer = SerializerFactory.createSerializer(this.serializerClass);
            return new BloomFilterConfiguration<>(this.bitSetSize, this.expectedNumberOfElements,
                    this.numberOfHashFunctions, this.falsePositiveProbability, this.hashingAlgorithm, this.bloomFilterType, serializer);
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new InvalidConfigurationException("Invalid serializer class", e);
        }
    }
}
