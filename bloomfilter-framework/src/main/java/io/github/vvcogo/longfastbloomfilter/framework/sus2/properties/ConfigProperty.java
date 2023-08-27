package io.github.vvcogo.longfastbloomfilter.framework.sus2.properties;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterConfigBuilder;

import java.util.Properties;

public interface ConfigProperty {

    String getPropertyName();

    boolean isSatisfied(Properties properties);

    void calculateValue(BloomFilterConfigBuilder builder, Properties properties);
}
