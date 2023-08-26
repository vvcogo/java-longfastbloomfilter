package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties;

import io.github.vvcogo.longfastbloomfilter.framework.sus.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.sus.InvalidConfigurationException;

import java.util.Optional;
import java.util.Properties;

public class ExpectedNumberOfElementsProperty implements OptionalProperty{

    public long[] checkProperty(Properties properties, double falsePositiveProbability, Optional bitSetSizeOpt) {
        if(properties.containsKey("expected-elements"){
            long expectedNumberOfElements = Long.parseLong(properties.getProperty("expected-elements"));
            long minBitSetSize = BloomFilterCalculations.calculateMinBitSetSize(expectedNumberOfElements, falsePositiveProbability);
            long bitSetSize;
            if(bitSetSizeOpt.isPresent()) {
                bitSetSize = (long) bitSetSizeOpt.get();
                if (bitSetSize < minBitSetSize) {
                    throw new InvalidConfigurationException("The specified bitset size is smaller than the minimum required for the specified expected number of elements and false positive probability");
                }
            }else {
                bitSetSize = minBitSetSize;
            }
            return new long[2]{expectedNumberOfElements, bitSetSize};
        }else {
            throw new InvalidConfigurationException("BloomFilter requires bitset size or expected elements to be specified!");
        }
    }
}
