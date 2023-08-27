package io.github.vvcogo.longfastbloomfilter.framework.sus2;

import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;

public class BloomFilterConfigBuilder<T> {

    private HashingAlgorithm hashingAlgorithm;
    private Serializer<? super T> serializer;
    private String bloomFilterType;
    private double falsePositiveProbability;
    private long expectedNumberOfElements;
    private long bitSetSize;
    private int numberOfHashFunctions;

    public BloomFilterConfigBuilder(){
    }

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

    public BloomFilterConfigBuilder<T> setSerializer(Serializer<? super T> serializer){
        this.serializer = serializer;
        return this;
    }

    public BloomFilterConfiguration<T> build() {
        return new BloomFilterConfiguration<>(this.bitSetSize, this.expectedNumberOfElements,
                this.numberOfHashFunctions, this.falsePositiveProbability, this.hashingAlgorithm, this.bloomFilterType, this.serializer);
    }
}
