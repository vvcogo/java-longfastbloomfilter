package io.github.vvcogo.longfastbloomfilter.framework.sus2;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.ConfigProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BloomFilterConfigurationLoader {

    List<ConfigProperty> propList = new ArrayList<>();

    public boolean isVaid(){
        for (ConfigProperty propElem: this.propList) {
            //
        }
        return true;
    }

    BloomFilterConfiguration loadConfig(Properties properties) {
        isVaid();
        BloomFilterConfigBuilder builder = new BloomFilterConfigBuilder();
        for (ConfigProperty configPropertyElem : propList)
            configPropertyElem.calculateValue(builder, properties);
        return builder.build();
    }
}
