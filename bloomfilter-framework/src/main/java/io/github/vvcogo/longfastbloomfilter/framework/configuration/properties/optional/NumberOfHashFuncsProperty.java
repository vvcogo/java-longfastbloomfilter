package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperties;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.PropertyParams;

public class NumberOfHashFuncsProperty extends AbstractOptionalConfigProperty<Integer> {

    public NumberOfHashFuncsProperty() {
        super(ConfigProperties.NUMBER_HASH_FUNCTIONS.getName());
        addPropertyParameter(new PropertyParams<>(new String[]{ConfigProperties.BITSET_SIZE.getName(),ConfigProperties.EXPECTED_ELEMS.getName()}, props -> {
            long m = Long.parseLong(props.getProperty(ConfigProperties.BITSET_SIZE.getName()));
            long n = Long.parseLong(props.getProperty(ConfigProperties.EXPECTED_ELEMS.getName()));
            return BloomFilterCalculations.calculateOptimalNumberOfHashFunctions(m,n);
        }));
    }

}
