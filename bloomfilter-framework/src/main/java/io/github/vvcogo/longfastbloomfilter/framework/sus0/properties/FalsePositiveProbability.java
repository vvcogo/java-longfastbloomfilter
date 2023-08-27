package io.github.vvcogo.longfastbloomfilter.framework.sus0.properties;

import io.github.vvcogo.longfastbloomfilter.framework.sus2.BloomFilterCalculations;

import java.util.Properties;

public class FalsePositiveProbability implements OptionalProperty{

    public double checkProperty(Properties properties, long bitSetSize, long expectedNumberOfElements) {
        double falsePositiveProb;
        if (properties.containsKey("false-positive-probability")) {
            falsePositiveProb = Double.parseDouble(properties.getProperty("false-positive-probability"));
        } else {
            falsePositiveProb = BloomFilterCalculations.calculateFalsePositiveProbability(bitSetSize, expectedNumberOfElements);
        }
        return falsePositiveProb;
    }
}
