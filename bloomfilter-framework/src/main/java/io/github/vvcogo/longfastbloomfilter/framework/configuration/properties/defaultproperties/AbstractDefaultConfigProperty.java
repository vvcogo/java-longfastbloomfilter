package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.defaultproperties;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperty;

import java.util.Properties;

public abstract class AbstractDefaultConfigProperty implements ConfigProperty {

    private final String propertyName;
    private final String defaultValue;

    protected AbstractDefaultConfigProperty(String propertyName, String defaultValue) {
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    @Override
    public final String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public final boolean isSatisfied(Properties properties) {
        return true;
    }

    @Override
    public void calculateValue(Properties properties) {
        String value = properties.getProperty(getPropertyName(), this.defaultValue);
        properties.setProperty(getPropertyName(), value);
    }
}
