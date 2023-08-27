package io.github.vvcogo.longfastbloomfilter.framework.sus0.properties;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.InvalidConfigurationException;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashFunction;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;

import java.util.Properties;

public class HashFunctionProperty implements OptionalProperty{

    public HashingAlgorithm checkProperty(Properties properties) {
        String hashFunction = properties.getProperty("hash-function", HashFunction.MURMUR_KIRSCH_MITZENMACHER.toString());
        try {
            return HashFunction.valueOf(hashFunction).getHashingAlgorithm();
        } catch (IllegalArgumentException e) {
            throw new InvalidConfigurationException("Specified hash function does not exists!", e);
        }
    }


}
