package io.github.vvcogo.longfastbloomfilter.framework.sus1;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.properties.ConfigProperty;

public abstract class PropertyElement implements ConfigProperty {

    private String propName;

    protected PropertyElement(String propName){
        this.propName = propName;
    }

    @Override
    public String getPropertyName() {
        return this.propName;
    }

}
