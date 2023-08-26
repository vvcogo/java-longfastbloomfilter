package io.github.vvcogo.longfastbloomfilter.framework.sus;

import java.util.Properties;

public class RequiredProperty extends PropertyElement {

    public RequiredProperty(String propName){
        super(propName);
    }

    @Override
    public boolean isSatisfied(Properties properties) {
        return properties.containsKey(getPropertyName());
    }

    @Override
    public void calculateValue() {
        throw new InvalidConfigurationException("The required property need to be specified!")
    }
}
