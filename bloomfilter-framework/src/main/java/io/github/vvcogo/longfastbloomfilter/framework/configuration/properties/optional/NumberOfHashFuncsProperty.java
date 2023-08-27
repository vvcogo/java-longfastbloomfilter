package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigBuilder;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.PropertyParams;

import java.util.Optional;
import java.util.Properties;

public class NumberOfHashFuncsProperty extends AbstractOptionalConfigProperty<Integer> {

    public NumberOfHashFuncsProperty() {
        super("number-hash-functions");
        addPropertyParameter(new PropertyParams<>(new String[]{"bitset-size","expected-elements"}, props -> {
            long m = Long.parseLong(props.getProperty("bitset-size"));
            long n = Long.parseLong(props.getProperty("expected-elements"));
            return BloomFilterCalculations.calculateOptimalNumberOfHashFunctions(m,n);
        }));
    }

    @Override
    public void calculateValue(BloomFilterConfigBuilder<?> builder, Properties properties) {
        Optional<PropertyParams<Integer>> optionalParam = getFirstSatisfiedProperty(properties);
        Integer value = optionalParam.get().getCalculateFunc().apply(properties);
        builder.setNumberOfHashFunctions(value);
    }


}
