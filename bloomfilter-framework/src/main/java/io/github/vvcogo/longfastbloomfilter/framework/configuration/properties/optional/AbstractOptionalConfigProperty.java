package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperty;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.PropertyParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public abstract class AbstractOptionalConfigProperty<T> implements ConfigProperty {

    private final List<PropertyParams<T>> pairListParams = new ArrayList<>();
    private final String propertyName;

    protected AbstractOptionalConfigProperty(String name) {
        this.propertyName = name;
    }

    @Override
    public final boolean isSatisfied(Properties properties) {
        if(properties.containsKey(this.propertyName))
            return true;
        Optional<PropertyParams<T>> optionalParam = getFirstSatisfiedProperty(properties);
        return optionalParam.isPresent();
    }

    @Override
    public final String getPropertyName() {
        return this.propertyName;
    }

    protected final Optional<PropertyParams<T>> getFirstSatisfiedProperty(Properties properties) {
        for (PropertyParams<T> params : pairListParams) {
            if (params.isSatisfied(properties)) {
                return Optional.of(params);
            }
        }
        return Optional.empty();
    }

    protected final void addPropertyParameter(PropertyParams<T> param) {
        this.pairListParams.add(param);
    }
}
