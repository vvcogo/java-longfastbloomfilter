package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.PropertyParams;

public class FalsePositiveProbProperty extends AbstractOptionalConfigProperty<Double> {

    public FalsePositiveProbProperty() {
        super("false-positive-probability");
        addPropertyParameter(new PropertyParams<>(new String[]{"bitset-size","expected-elements"}, props -> {
            long m = Long.parseLong(props.getProperty("bitset-size"));
            long n = Long.parseLong(props.getProperty("expected-elements"));
            return BloomFilterCalculations.calculateFalsePositiveProbability(m,n);
        }));
    }
}
