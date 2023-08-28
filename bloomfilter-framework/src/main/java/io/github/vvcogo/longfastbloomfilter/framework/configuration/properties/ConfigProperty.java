package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties;

import java.util.Properties;

public interface ConfigProperty {

    String getPropertyName();

    boolean isSatisfied(Properties properties);

    void calculateValue(Properties properties);
}
