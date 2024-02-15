package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties;

public enum ConfigProperties {

    BITSET_SIZE("bitset-size"),
    EXPECTED_ELEMS("expected-elements"),
    FALSE_POSITIVE_PROBABILITY("false-positive-probability"),
    NUMBER_HASH_FUNCTIONS("number-hash-functions"),
    SERIALIZER("serializer"),
    HASH_FUNCTION("hash-function"),
    BLOOMFILTER_TYPE("bloomfilter-type"),
    SPEED_OPTIMIZATION("enable-speed-optimization"),
    MAX_SIZE_CHANGE("max-optimization-size-change")
    ;

    private final String name;

    ConfigProperties(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
