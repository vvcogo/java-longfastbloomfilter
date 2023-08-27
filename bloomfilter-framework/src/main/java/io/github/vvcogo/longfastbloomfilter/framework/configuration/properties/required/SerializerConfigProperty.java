package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.required;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigBuilder;

import java.util.Properties;

public class SerializerConfigProperty extends AbstractRequiredConfigProperty {

    public SerializerConfigProperty() {
        super("serializer");
    }

    @Override
    public void calculateValue(BloomFilterConfigBuilder<?> builder, Properties properties) {
        String serializerClassName = properties.getProperty(getPropertyName());
        builder.setSerializer(serializerClassName);
    }
}
