package io.github.vvcogo.longfastbloomfilter.framework.sus2.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class AbstractConfigProperty<T> implements ConfigProperty {

    protected final List<PropertyParams<T>> pairListParams;

    protected AbstractConfigProperty(){
        this.pairListParams = new ArrayList<>();
    }

    @Override
    public boolean isSatisfied(Properties properties) {
        int[] pairs = new int[numberOfConbinationsForCalculate()];
        if(properties.containsKey(getPropertyName()))
            return true;
        else {
            int index = 0;
            for (PropertyParams<T> propParam: this.pairListParams) {
                for (String pairElem : propParam.getValidPairList()){
                    if(!properties.containsKey(pairElem)){
                        pairs[index]++;
                    }
                }
                if(index < numberOfConbinationsForCalculate() - 1)
                    index++;
            }
        }
        boolean isValid = false;
        for (int i = 0; i < pairs.length; i++){
            if(pairs[i] == 0){
                this.pairListParams.get(i).setAvaliable(true);
                isValid = true;
            }
        }
        return isValid;
    }

    protected int numberOfConbinationsForCalculate(){
        return this.pairListParams.size();
    }
}
