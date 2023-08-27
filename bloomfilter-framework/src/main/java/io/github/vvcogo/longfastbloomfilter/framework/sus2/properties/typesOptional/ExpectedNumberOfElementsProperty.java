package io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.typesOptional;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterConfigBuilder;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.AbstractConfigProperty;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.PropertyParams;

import java.util.Properties;

public class ExpectedNumberOfElementsProperty extends AbstractConfigProperty {

    public ExpectedNumberOfElementsProperty() {
        super();
        this.pairListParams.add(new PropertyParams<>(new String[]{"m","p"}, props -> {
            long m = Long.parseLong(props.getProperty("bitset-size"));
            double p = Double.parseDouble(props.getProperty("false-positive-probability"));
            return BloomFilterCalculations.calculateMaxNumberOfElements(m,p);
        }));

        this.pairListParams.add(new PropertyParams<>(new String[]{"m","k"}, props -> {
            long m = Long.parseLong(props.getProperty("bitset-size"));
            int k = Integer.parseInt(props.getProperty("number-hash-functions"));
            return BloomFilterCalculations.calculateMaxNumberOfElements(m,k);
        }));
    }

    @Override
    public String getPropertyName() {
        return "expected-elements";
    }

    @Override
    public void calculateValue(BloomFilterConfigBuilder builder, Properties properties) {

    }
}
