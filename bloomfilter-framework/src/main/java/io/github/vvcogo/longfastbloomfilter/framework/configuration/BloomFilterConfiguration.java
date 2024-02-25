package io.github.vvcogo.longfastbloomfilter.framework.configuration;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperties;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashFactory;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.SerializerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public final class BloomFilterConfiguration<T> implements Serializable {

    private final long bitSetSize;
    private final long expectedNumberOfElements;
    private final int numberOfHashFunctions;
    private final double falsePositiveRate;
    private final HashingAlgorithm hashFunction;
    private final String bloomFilterType;
    private final Serializer<? super T> serializer;

    public BloomFilterConfiguration(long bitSetSize, long expectedNumberOfElements, int numberOfHashFunctions,
                                    double falsePositiveRate, HashingAlgorithm hashFunction,
                                    String bloomFilterType, Serializer<? super T> serializer) {
        this.bitSetSize = bitSetSize;
        this.expectedNumberOfElements = expectedNumberOfElements;
        this.numberOfHashFunctions = numberOfHashFunctions;
        this.falsePositiveRate = falsePositiveRate;
        this.hashFunction = hashFunction;
        this.bloomFilterType = bloomFilterType;
        this.serializer = serializer;
    }

    public long getBitSetSize() {
        return bitSetSize;
    }

    public long getExpectedNumberOfElements() {
        return expectedNumberOfElements;
    }

    public int getNumberOfHashFunctions() {
        return numberOfHashFunctions;
    }

    public double getFalsePositiveRate() {
        return falsePositiveRate;
    }

    public HashingAlgorithm getHashFunction() {
        return hashFunction;
    }

    public String getBloomFilterType() {
        return bloomFilterType;
    }

    public Serializer<? super T> getSerializer() {
        return serializer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BloomFilter Configuration:").append(System.lineSeparator())
                .append(" > BitSet Size: ").append(this.bitSetSize).append(System.lineSeparator())
                .append(" > Expected Number of Elements: ").append(this.expectedNumberOfElements).append(System.lineSeparator())
                .append(" > Number of Hash Functions: ").append(this.numberOfHashFunctions).append(System.lineSeparator())
                .append(" > False Positive Probability: ").append(this.falsePositiveRate).append(System.lineSeparator())
                .append(" > Hashing Algorihm: ").append(this.hashFunction.getClass().getSimpleName()).append(System.lineSeparator())
                .append(" > BloomFilter Type: ").append(this.bloomFilterType).append(System.lineSeparator())
                .append(" > Serializer class: ").append(this.serializer.getClass().getName());
        return sb.toString();
    }

    public static <E> BloomFilterConfiguration<E> fromProperties(Properties properties) throws InvalidConfigurationException {
        try {
            long bitSetSize = Long.parseLong(properties.getProperty(ConfigProperties.BITSET_SIZE.getName()));
            long expectedElems = Long.parseLong(properties.getProperty(ConfigProperties.EXPECTED_ELEMS.getName()));
            int numbHash = Integer.parseInt(properties.getProperty(ConfigProperties.NUMBER_HASH_FUNCTIONS.getName()));
            double fpp = Double.parseDouble(properties.getProperty(ConfigProperties.FALSE_POSITIVE_PROBABILITY.getName()));
            HashingAlgorithm hashingAlgorithm = HashFactory.getHashingAlgorithm(properties.getProperty(ConfigProperties.HASH_FUNCTION.getName()));
            String type = properties.getProperty(ConfigProperties.BLOOMFILTER_TYPE.getName());
            Serializer<? super E> serializer = SerializerFactory.createSerializer(properties.getProperty(ConfigProperties.SERIALIZER.getName()));
            String speedOptimization = ConfigProperties.SPEED_OPTIMIZATION.getName();
            if (properties.containsKey(speedOptimization) && properties.getProperty(speedOptimization).equalsIgnoreCase("true")) {
                double maxSizeChange = 0.2;
                if (properties.containsKey(ConfigProperties.MAX_SIZE_CHANGE.getName()))
                    maxSizeChange = Double.parseDouble(ConfigProperties.MAX_SIZE_CHANGE.getName());
                long tmpBitSetSize = bitSetSize;
                long maxChange = (long) (maxSizeChange * bitSetSize + bitSetSize);
                int tmpK = numbHash-1;
                while (tmpK > 0) {
                    // (-k * n) / ln(-p^(1/k) + 1)
                    tmpBitSetSize = (long) ((-tmpK*expectedElems)/Math.log(-Math.pow(fpp,1/(double)tmpK)+1));
                    if (tmpBitSetSize < maxChange && tmpBitSetSize > 0) {
                        bitSetSize = tmpBitSetSize;
                        numbHash = tmpK;
                    } else
                        break;
                    tmpK--;
                }
            }
            return new BloomFilterConfiguration<>(bitSetSize, expectedElems, numbHash, fpp, hashingAlgorithm, type, serializer);
        } catch (NumberFormatException | NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new InvalidConfigurationException(e);
        }
    }
}
