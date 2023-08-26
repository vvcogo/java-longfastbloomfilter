package io.github.vvcogo.longfastbloomfilter.framework.sus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class OptionalProperty extends PropertyElement{

    private PropertyParams paramProp;
    private static final List<String> LIST_OPTIONALS = new ArrayList<>(Arrays.asList("expected-elements", "bitset-size","false-positive-probability", "hash-function"));

    public OptionalProperty(String propName){
        super(propName);
    }

    @Override
    public boolean isSatisfied(Properties properties) {
        //m = np  // k = mn  // n = mp  // p = mn
        return properties.containsKey("expected-elements") && properties.containsKey("false-positive-probability") ||
                        properties.containsKey("bitset-size") && properties.containsKey("expected-elements") ||
                        properties.containsKey("bitset-size") && properties.containsKey("false-positive-probability");
    }

    @Override
    public void calculateValue() {
        paramProp.getCalculateFunc();
    }

    public void setPropParams(PropertyParams params){
        this.paramProp = params;
    }
}
