package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.defaultproperties;

import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashFunction;

public class HashFunctionConfigProperty extends AbstractDefaultConfigProperty {

    private static final String DEFAULT_HASHFUNCTION_NAME = HashFunction.MURMUR_KIRSCH_MITZENMACHER.name();

    public HashFunctionConfigProperty() {
        super("hash-function", DEFAULT_HASHFUNCTION_NAME);
    }
}
