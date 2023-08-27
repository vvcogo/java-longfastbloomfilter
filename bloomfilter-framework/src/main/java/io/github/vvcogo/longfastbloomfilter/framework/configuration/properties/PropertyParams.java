package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties;

import java.util.Properties;
import java.util.function.Function;

public class PropertyParams<T>{

    private final String[] validPairList;
    private final Function<Properties, T> calculateFunc;

    public PropertyParams(String[] validPairList, Function<Properties, T> calculateFunc){
        this.validPairList = validPairList;
        this.calculateFunc = calculateFunc;
    }

    public Function<Properties, T> getCalculateFunc() {
        return calculateFunc;
    }

    public boolean isSatisfied(Properties properties) {
        for (String property : validPairList) {
            if (!properties.containsKey(property)) {
                return false;
            }
        }
        return true;
    }
}
