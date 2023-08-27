package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.defaultproperties;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperty;

import java.util.Properties;

public abstract class AbstractDefaultConfigProperty implements ConfigProperty {

    private final String propertyName;

    protected AbstractDefaultConfigProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public final String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public final boolean isSatisfied(Properties properties) {
        return true;
    }
}
