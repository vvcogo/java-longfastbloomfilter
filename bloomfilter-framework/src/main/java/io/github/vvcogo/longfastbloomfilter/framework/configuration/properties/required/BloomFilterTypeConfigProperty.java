package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.required;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigBuilder;

import java.util.Properties;

public class BloomFilterTypeConfigProperty extends AbstractRequiredConfigProperty {

    public BloomFilterTypeConfigProperty() {
        super("bloomfilter-type");
    }

    @Override
    public void calculateValue(BloomFilterConfigBuilder<?> builder, Properties properties) {
        String type = properties.getProperty(getPropertyName());
        builder.setBloomFilterType(type);
    }
}
