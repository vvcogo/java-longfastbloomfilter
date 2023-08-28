package io.github.vvcogo.longfastbloomfilter.framework.configuration;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.ConfigProperty;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.defaultproperties.HashFunctionConfigProperty;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional.BitSetSizeProperty;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional.ExpectedNumberOfElementsProperty;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional.FalsePositiveProbProperty;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.optional.NumberOfHashFuncsProperty;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.required.BloomFilterTypeConfigProperty;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.properties.required.SerializerConfigProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BloomFilterConfigurationLoader<T> {

    private final List<ConfigProperty> propList = new ArrayList<>();

    private BloomFilterConfigurationLoader() {
    }

    public BloomFilterConfiguration<T> loadConfig(Properties properties) throws InvalidConfigurationException {
        Properties copy = (Properties) properties.clone();
        List<ConfigProperty> unsatisfiedProperties = new ArrayList<>(propList);
        boolean changed;
        do {
            changed = false;
            List<ConfigProperty> newUnsatisfied = new ArrayList<>();
            for (ConfigProperty property : unsatisfiedProperties) {
                if (property.isSatisfied(copy)) {
                    changed = true;
                    property.calculateValue(copy);
                } else {
                    newUnsatisfied.add(property);
                }
            }
            unsatisfiedProperties = newUnsatisfied;
        } while (changed && !unsatisfiedProperties.isEmpty());
        if (!unsatisfiedProperties.isEmpty()) {
            throw new InvalidConfigurationException("Unsatisfied properties: " + unsatisfiedProperties);
        }
        return BloomFilterConfiguration.fromProperties(copy);
    }

    public static <E> BloomFilterConfigurationLoader<E> emptyLoader() {
        return new BloomFilterConfigurationLoader<>();
    }

    public static <E> BloomFilterConfigurationLoader<E> defaultLoader() {
        BloomFilterConfigurationLoader<E> loader = emptyLoader();
        loader.loadDefaultProperties();
        return loader;
    }

    public void addConfigProperty(ConfigProperty property) {
        this.propList.add(property);
    }

    private void loadDefaultProperties() {
        addConfigProperty(new BitSetSizeProperty());
        addConfigProperty(new ExpectedNumberOfElementsProperty());
        addConfigProperty(new FalsePositiveProbProperty());
        addConfigProperty(new NumberOfHashFuncsProperty());
        addConfigProperty(new HashFunctionConfigProperty());
        addConfigProperty(new BloomFilterTypeConfigProperty());
        addConfigProperty(new SerializerConfigProperty());
    }
}
