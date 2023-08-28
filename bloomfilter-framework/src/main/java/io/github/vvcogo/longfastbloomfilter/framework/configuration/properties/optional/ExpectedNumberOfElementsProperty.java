package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.PropertyParams;

public class ExpectedNumberOfElementsProperty extends AbstractOptionalConfigProperty<Long> {

    public ExpectedNumberOfElementsProperty() {
        super("expected-elements");
        addPropertyParameter(new PropertyParams<>(new String[]{"bitset-size","false-positive-probability"}, props -> {
            long m = Long.parseLong(props.getProperty("bitset-size"));
            double p = Double.parseDouble(props.getProperty("false-positive-probability"));
            return BloomFilterCalculations.calculateMaxNumberOfElements(m,p);
        }));

        addPropertyParameter(new PropertyParams<>(new String[]{"bitset-size","number-hash-functions"}, props -> {
            long m = Long.parseLong(props.getProperty("bitset-size"));
            int k = Integer.parseInt(props.getProperty("number-hash-functions"));
            return BloomFilterCalculations.calculateMaxNumberOfElements(m,k);
        }));
    }
}
