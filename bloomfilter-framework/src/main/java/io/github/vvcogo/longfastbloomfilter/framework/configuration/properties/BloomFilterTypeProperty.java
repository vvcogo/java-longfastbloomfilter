package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties;

import io.github.vvcogo.longfastbloomfilter.framework.sus.InvalidConfigurationException;

import java.util.Properties;

public class BloomFilterTypeProperty implements RequiredProperty{

    public void checkProperty(Properties properties) {
        if (!properties.containsKey("bloomfilter-type")) {
            throw new InvalidConfigurationException("Type of bloomfilter is not specified!");
        }
    }
}
