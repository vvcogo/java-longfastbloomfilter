package io.github.vvcogo.longfastbloomfilter.framework.sus0.properties;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.InvalidConfigurationException;

import java.util.Properties;

public class BloomFilterTypeProperty implements RequiredProperty{

    public void checkProperty(Properties properties) {
        if (!properties.containsKey("bloomfilter-type")) {
            throw new InvalidConfigurationException("Type of bloomfilter is not specified!");
        }
    }
}
