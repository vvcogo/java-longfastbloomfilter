package io.github.vvcogo.longfastbloomfilter.framework.sus;

public abstract class PropertyElement implements Property{

    private String propName;

    protected PropertyElement(String propName){
        this.propName = propName;
    }

    @Override
    public String getPropertyName() {
        return this.propName;
    }

}
