package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.defaultproperties;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperties;

public class HashFunctionConfigProperty extends AbstractDefaultConfigProperty {

    private static final String DEFAULT_HASHFUNCTION_NAME = "murmur_kirschmitz";

    public HashFunctionConfigProperty() {
        super(ConfigProperties.HASH_FUNCTION.getName(), DEFAULT_HASHFUNCTION_NAME);
    }
}
