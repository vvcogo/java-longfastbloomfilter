package io.github.vvcogo.longfastbloomfilter.framework.sus2.properties;

import java.util.Properties;
import java.util.function.Function;

public class PropertyParams<T>{

    private String[] validPairList;
    private Function<Properties, T> calculateFunc;
    private boolean isAvaliable = false;

    public PropertyParams(String[] validPairList, Function<Properties, T> calculateFunc){
        this.validPairList = validPairList;
        this.calculateFunc = calculateFunc;
    }

    public Function<Properties, T> getCalculateFunc() {
        return calculateFunc;
    }

    public String[] getValidPairList() {
        return validPairList;
    }

    public boolean isAvaliable() {
        return this.isAvaliable;
    }

    public void setAvaliable(boolean isAvaliable) {
        this.isAvaliable = isAvaliable;
    }

}
