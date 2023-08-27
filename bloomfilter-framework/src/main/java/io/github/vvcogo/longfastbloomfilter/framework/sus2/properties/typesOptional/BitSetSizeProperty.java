package io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.typesOptional;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterCalculations;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterConfigBuilder;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.AbstractConfigProperty;
import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.PropertyParams;

import java.util.Properties;

public class BitSetSizeProperty extends AbstractConfigProperty<Long> {

    public BitSetSizeProperty() {
        super();
        this.pairListParams.add(new PropertyParams<>(new String[]{"expected-elements","false-positive-probability"}, props -> {
            long n = Long.parseLong(props.getProperty("expected-elements"));
            double p = Double.parseDouble(props.getProperty("false-positive-probability"));
            return BloomFilterCalculations.calculateMinBitSetSize(n,p);
        }));
    }

    @Override
    public String getPropertyName() {
        return "bitset-size";
    }

    @Override
    public void calculateValue (BloomFilterConfigBuilder builder, Properties properties) {
        long bitsetSize;
        for (PropertyParams<Long> paramElem : this.pairListParams){
            if(paramElem.isAvaliable()){
                bitsetSize = paramElem.getCalculateFunc().apply(properties);
                builder.setBitSetSize(bitsetSize);
            }
        }
    }

}
