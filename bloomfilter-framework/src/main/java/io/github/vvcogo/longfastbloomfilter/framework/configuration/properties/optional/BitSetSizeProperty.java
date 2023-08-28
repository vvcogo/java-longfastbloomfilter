package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.PropertyParams;

public class BitSetSizeProperty extends AbstractOptionalConfigProperty<Long> {

    public BitSetSizeProperty() {
        super("bitset-size");
        addPropertyParameter(new PropertyParams<>(new String[]{"expected-elements","false-positive-probability"}, props -> {
            long n = Long.parseLong(props.getProperty("expected-elements"));
            double p = Double.parseDouble(props.getProperty("false-positive-probability"));
            return BloomFilterCalculations.calculateMinBitSetSize(n,p);
        }));
    }

}
