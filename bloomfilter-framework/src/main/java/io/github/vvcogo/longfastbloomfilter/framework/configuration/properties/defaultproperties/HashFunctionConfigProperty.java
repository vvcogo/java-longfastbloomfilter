package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.defaultproperties;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigBuilder;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashFunction;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;

import java.util.Properties;

public class HashFunctionConfigProperty extends AbstractDefaultConfigProperty {

    private static final String DEFAULT_HASHFUNCTION_NAME = HashFunction.MURMUR_KIRSCH_MITZENMACHER.name();

    public HashFunctionConfigProperty() {
        super("hash-function");
    }

    @Override
    public void calculateValue(BloomFilterConfigBuilder<?> builder, Properties properties) {
        String hashFunctionName = properties.getProperty(getPropertyName(), DEFAULT_HASHFUNCTION_NAME);
        HashFunction hashFunction = HashFunction.valueOf(hashFunctionName);
        HashingAlgorithm hashingAlgorithm = hashFunction.getHashingAlgorithm();
        builder.setHashingAlgorithm(hashingAlgorithm);
    }
}
