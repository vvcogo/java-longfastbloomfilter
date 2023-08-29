package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperties;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.PropertyParams;

public class BitSetSizeProperty extends AbstractOptionalConfigProperty<Long> {

    public BitSetSizeProperty() {
        super(ConfigProperties.BITSET_SIZE.getName());
        addPropertyParameter(new PropertyParams<>(new String[]{ConfigProperties.EXPECTED_ELEMS.getName(),ConfigProperties.FALSE_POSITIVE_PROBABILITY.getName()}, props -> {
            long n = Long.parseLong(props.getProperty(ConfigProperties.EXPECTED_ELEMS.getName()));
            double p = Double.parseDouble(props.getProperty(ConfigProperties.FALSE_POSITIVE_PROBABILITY.getName()));
            return BloomFilterCalculations.calculateMinBitSetSize(n,p);
        }));
    }

}
