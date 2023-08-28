package io.github.vvcogo.longfastbloomfilter.framework.configuration;

import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashFunction;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.SerializerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public record BloomFilterConfiguration<T>(long bitSetSize,
                                          long expectedNumberOfElements,
                                          int numberOfHashFunctions,
                                          double falsePositiveRate,
                                          HashingAlgorithm hashFunction,
                                          String bloomFilterType,
                                          Serializer<? super T> serializer) implements Serializable {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BloomFilter Configuration:").append(System.lineSeparator())
                .append(" > BitSet Size: ").append(bitSetSize).append(System.lineSeparator())
                .append(" > Expected Number of Elements: ").append(expectedNumberOfElements).append(System.lineSeparator())
                .append(" > Number of Hash Functions: ").append(numberOfHashFunctions).append(System.lineSeparator())
                .append(" > False Positive Probability: ").append(falsePositiveRate).append(System.lineSeparator())
                .append(" > Hashing Algorihm: ").append(hashFunction.getClass().getSimpleName()).append(System.lineSeparator())
                .append(" > BloomFilter Type: ").append(bloomFilterType).append(System.lineSeparator())
                .append(" > Serializer class: ").append(serializer.getClass().getName());
        return sb.toString();
    }

    public static <E> BloomFilterConfiguration<E> fromProperties(Properties properties) throws InvalidConfigurationException {
        try {
            long bitSetSize = Long.parseLong(properties.getProperty("bitset-size"));
            long expectedElems = Long.parseLong(properties.getProperty("expected-elements"));
            int numbHash = Integer.parseInt(properties.getProperty("number-hash-functions"));
            double fpp = Double.parseDouble(properties.getProperty("false-positive-probability"));
            HashingAlgorithm hashingAlgorithm = HashFunction.valueOf(properties.getProperty("hash-function")).getHashingAlgorithm();
            String type = properties.getProperty("bloomfilter-type");
            Serializer<? super E> serializer = SerializerFactory.createSerializer(properties.getProperty("serializer"));
            return new BloomFilterConfiguration<>(bitSetSize, expectedElems, numbHash, fpp, hashingAlgorithm, type, serializer);
        } catch (NumberFormatException | NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new InvalidConfigurationException(e);
        }
    }
}
