package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfigBuilder;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.PropertyParams;

import java.util.Optional;
import java.util.Properties;

public class BitSetSizeProperty extends AbstractOptionalConfigProperty<Long> {

    public BitSetSizeProperty() {
        super("bitset-size");
        addPropertyParameter(new PropertyParams<>(new String[]{"expected-elements","false-positive-probability"}, props -> {
            long n = Long.parseLong(props.getProperty("expected-elements"));
            double p = Double.parseDouble(props.getProperty("false-positive-probability"));
            return BloomFilterCalculations.calculateMinBitSetSize(n,p);
        }));
    }

    @Override
    public void calculateValue(BloomFilterConfigBuilder<?> builder, Properties properties) {
        Optional<PropertyParams<Long>> optionalParam = getFirstSatisfiedProperty(properties);
        Long value = optionalParam.get().getCalculateFunc().apply(properties);
        builder.setBitSetSize(value);
    }

}
