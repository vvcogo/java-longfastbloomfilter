package io.github.vvcogo.longfastbloomfilter.framework.sus;

import java.util.Properties;
import java.util.function.Function;

public class PropertyParams{

    private String[] validPairList;
    private Function<Properties, Long> calculateFunc;

    public PropertyParams(String[] validPairList, Function<Properties, Long> calculateFunc){
        this.validPairList = validPairList;
        this.calculateFunc = calculateFunc;
    }

    public Function<Properties, Long> getCalculateFunc() {
        return calculateFunc;
    }

    public String[] getValidPairList() {
        return validPairList;
    }
}
