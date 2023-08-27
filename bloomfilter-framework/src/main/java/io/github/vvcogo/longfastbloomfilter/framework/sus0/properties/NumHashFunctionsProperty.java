package io.github.vvcogo.longfastbloomfilter.framework.sus0.properties;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.InvalidConfigurationException;

import java.util.Properties;

public class NumHashFunctionsProperty implements OptionalProperty{

    public int checkProperty(Properties properties, long bitSetSize, long expectedNumberOfElements) {
        int numberOfHashFunctions;
        int optimalHash = BloomFilterCalculations.calculateOptimalNumberOfHashFunctions(bitSetSize, expectedNumberOfElements);
        if (properties.containsKey("number-hash-functions")) {
            numberOfHashFunctions = Integer.parseInt(properties.getProperty("number-hash-functions"));
            if(numberOfHashFunctions < optimalHash)
                throw new InvalidConfigurationException("The specified number of hash functions must be greater for the specified bitset size and expected number of elements!");
        } else {
            numberOfHashFunctions = optimalHash;
        }
        return numberOfHashFunctions;
    }
}
