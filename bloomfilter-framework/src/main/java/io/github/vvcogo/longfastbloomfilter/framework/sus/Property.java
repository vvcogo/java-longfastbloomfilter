package io.github.vvcogo.longfastbloomfilter.framework.sus;

import java.util.Properties;

public interface Property {

    String getPropertyName();

    boolean isSatisfied(Properties properties);

    void calculateValue();
}
