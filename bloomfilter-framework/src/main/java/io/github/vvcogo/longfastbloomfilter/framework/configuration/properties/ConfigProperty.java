package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigBuilder;

import java.util.Properties;

public interface ConfigProperty {

    String getPropertyName();

    boolean isSatisfied(Properties properties);

    void calculateValue(BloomFilterConfigBuilder<?> builder, Properties properties);
}
