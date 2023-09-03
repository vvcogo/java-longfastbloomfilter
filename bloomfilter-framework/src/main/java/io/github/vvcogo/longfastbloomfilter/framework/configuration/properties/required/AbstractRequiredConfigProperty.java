package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.required;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperty;

import java.util.Properties;

public abstract class AbstractRequiredConfigProperty implements ConfigProperty {

    private final String propertyName;

    protected AbstractRequiredConfigProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public final String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public final boolean isSatisfied(Properties properties) {
        return properties.containsKey(this.propertyName);
    }

    @Override
    public void calculateValue(Properties properties) {
    }
}
