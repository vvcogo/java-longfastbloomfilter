package io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.typesOptional;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterConfigBuilder;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.AbstractConfigProperty;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.PropertyParams;

import java.util.Properties;

public class NumberOfHashFuncsProperty extends AbstractConfigProperty<Integer> {

    public NumberOfHashFuncsProperty() {
        super();
        this.pairListParams.add(new PropertyParams<>(new String[]{"m","n"}, props -> {
            long m = Long.parseLong(props.getProperty("bitset-size"));
            long n = Long.parseLong(props.getProperty("expected-elements"));
            return BloomFilterCalculations.calculateOptimalNumberOfHashFunctions(m,n);
        }));
    }

    @Override
    public String getPropertyName() {
        return "number-hash-functions";
    }

    @Override
    public void calculateValue(BloomFilterConfigBuilder builder, Properties properties) {

    }


}
