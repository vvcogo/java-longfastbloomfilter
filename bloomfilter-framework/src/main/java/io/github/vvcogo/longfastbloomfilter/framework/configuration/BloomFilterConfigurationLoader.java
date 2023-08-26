package io.github.vvcogo.longfastbloomfilter.framework.configuration;

import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashFunction;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.ObjectSerializer;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.SerializerFactory;
import io.github.vvcogo.longfastbloomfilter.framework.sus.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.sus.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.sus.InvalidConfigurationException;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class BloomFilterConfigurationLoader<T> {

    private HashingAlgorithm hashingAlgorithm;
    private Serializer<? super T> serializer;
    private final String bloomFilterType;
    private final double falsePositiveProbability;
    private long expectedNumberOfElements;
    private long bitSetSize;
    private int numberOfHashFunctions;
    private final String[] optionalConfig = {"bitset-size", "expected-elements", "number-hash-functions", "false-positive-probability"};

    public BloomFilterConfigurationLoader(Properties properties) {
        checkRequiredProperties(properties);
        checkOptionalProperties(properties);
        checkHashFunction(properties);
        checkSerializer(properties);
        this.bloomFilterType = properties.getProperty("bloomfilter-type");
        try {
            this.falsePositiveProbability = Double.parseDouble(properties.getProperty("false-positive-probability"));
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("False positive probability needs to be a number!", e);
        }
        checkBitsetSizeAndExpectedElements(properties);
        checkNumHashFunc(properties);
    }

    public BloomFilterConfiguration<T> getConfiguration() {
        return new BloomFilterConfiguration<>(this.bitSetSize, this.expectedNumberOfElements,
                this.numberOfHashFunctions, this.falsePositiveProbability, this.hashingAlgorithm, this.bloomFilterType, this.serializer);
    }


    private void checkOptionalProperties(Properties properties) {
        int numberOfOptionalProps = 0;
        for (String elemArr : optionalConfig) {
            if(properties.containsKey(elemArr))
                numberOfOptionalProps++;
        }
        if(numberOfOptionalProps < 2)
            throw new InvalidConfigurationException("The number of optional parameters specified in the configuration must be at least 2!");
    }

    private void checkHashFunction(Properties properties){
        String hashFunction = properties.getProperty("hash-function", HashFunction.MURMUR_KIRSCH_MITZENMACHER.toString());
        try {
            this.hashingAlgorithm = HashFunction.valueOf(hashFunction).getHashingAlgorithm();
        } catch (IllegalArgumentException e) {
            throw new InvalidConfigurationException("Specified hash function does not exists!", e);
        }
    }

    private void checkSerializer(Properties properties) {
        String serializerClass = properties.getProperty("serializer", ObjectSerializer.class.toString());
        try {
            this.serializer = SerializerFactory.createSerializer(serializerClass);
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new InvalidConfigurationException("Failed to create instance of specified serializer", e);
        }
    }

    private void checkBitsetSizeAndExpectedElements(Properties properties) {
        if (properties.containsKey("bitset-size")) {
            this.bitSetSize = Long.parseLong(properties.getProperty("bitset-size"));
            long maxElements = BloomFilterCalculations.calculateMaxNumberOfElements(this.bitSetSize, this.falsePositiveProbability);
            if (!properties.containsKey("expected-elements")) {
                this.expectedNumberOfElements = maxElements;
            } else {
                this.expectedNumberOfElements = Long.parseLong(properties.getProperty("expected-elements"));
                if (this.expectedNumberOfElements > maxElements) {
                    throw new InvalidConfigurationException("The specified number of expected elements is greater than the max allowed for the specified bitset size and false positive probability");
                }
            }
        } else if (properties.containsKey("expected-elements")) {
            this.expectedNumberOfElements = Long.parseLong(properties.getProperty("expected-elements"));
            long minBitSetSize = BloomFilterCalculations.calculateMinBitSetSize(this.expectedNumberOfElements, this.falsePositiveProbability);
            if (!properties.containsKey("bitset-size")) {
                this.bitSetSize = minBitSetSize;
            } else {
                this.bitSetSize = Long.parseLong(properties.getProperty("bitset-size"));
                if (this.bitSetSize < minBitSetSize) {
                    throw new InvalidConfigurationException("The specified bitset size is smaller than the minimum required for the specified expected number of elements and false positive probability");
                }
            }
        } else {
            throw new InvalidConfigurationException("BloomFilter requires bitset size or expected elements to be specified!");
        }
    }

    private void checkNumHashFunc(Properties properties) {
        int optimalHash = BloomFilterCalculations.calculateOptimalNumberOfHashFunctions(this.bitSetSize, this.expectedNumberOfElements);
        if (properties.containsKey("number-hash-functions")) {
            this.numberOfHashFunctions = Integer.parseInt(properties.getProperty("number-hash-functions"));
            if(this.numberOfHashFunctions < optimalHash)
                throw new InvalidConfigurationException("The specified number of hash functions must be greater for the specified bitset size and expected number of elements!");
        } else {
            this.numberOfHashFunctions = optimalHash;
        }
    }
}
