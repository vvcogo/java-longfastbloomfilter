package io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.typesOptional;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterConfigBuilder;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.AbstractConfigProperty;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.PropertyParams;

import java.util.Properties;

public class FalsePositiveProbProperty extends AbstractConfigProperty<Double> {

    public FalsePositiveProbProperty() {
        super();
        this.pairListParams.add(new PropertyParams<>(new String[]{"m","n"}, props -> {
            long m = Long.parseLong(props.getProperty("bitset-size"));
            long n = Long.parseLong(props.getProperty("expected-elements"));
            return BloomFilterCalculations.calculateFalsePositiveProbability(m,n);
        }));
    }

    @Override
    public String getPropertyName() {
        return "false-positive-probability";
    }

    @Override
    public void calculateValue(BloomFilterConfigBuilder builder, Properties properties) {

    }
}
