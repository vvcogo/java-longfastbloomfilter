package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperties;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.PropertyParams;

public class ExpectedNumberOfElementsProperty extends AbstractOptionalConfigProperty<Long> {

    public ExpectedNumberOfElementsProperty() {
        super(ConfigProperties.EXPECTED_ELEMS.getName());
        addPropertyParameter(new PropertyParams<>(new String[]{ConfigProperties.BITSET_SIZE.getName(),ConfigProperties.FALSE_POSITIVE_PROBABILITY.getName()}, props -> {
            long m = Long.parseLong(props.getProperty(ConfigProperties.BITSET_SIZE.getName()));
            double p = Double.parseDouble(props.getProperty(ConfigProperties.FALSE_POSITIVE_PROBABILITY.getName()));
            return BloomFilterCalculations.calculateMaxNumberOfElements(m,p);
        }));

        addPropertyParameter(new PropertyParams<>(new String[]{ConfigProperties.BITSET_SIZE.getName(),ConfigProperties.NUMBER_HASH_FUNCTIONS.getName()}, props -> {
            long m = Long.parseLong(props.getProperty(ConfigProperties.BITSET_SIZE.getName()));
            int k = Integer.parseInt(props.getProperty(ConfigProperties.NUMBER_HASH_FUNCTIONS.getName()));
            return BloomFilterCalculations.calculateMaxNumberOfElements(m,k);
        }));
    }
}
