package io.github.vvcogo.longfastbloomfilter.framework;


public final class BloomFilterCalculations {

    private static final double LN_2_SQUARED = Math.pow(Math.log(2), 2);

    private BloomFilterCalculations() {
        throw new UnsupportedOperationException("Cannot created instance of " + getClass().getName());
    }

    // m = -(n * ln(p) / ln(2) ^ 2)
    public static long calculateMinBitSetSize(long expectedNumberOfElements, double falsePositiveProbability) {
        double numerator = expectedNumberOfElements * Math.log(falsePositiveProbability);
        return (long) -(numerator / LN_2_SQUARED);
    }

    // k = ln(2) * m/n
    public static int calculateOptimalNumberOfHashFunctions(long bitSetSize, long expectedNumberOfElements) {
        return (int) (LN_2_SQUARED * bitSetSize / expectedNumberOfElements);
    }

    // n = -(m * ln(2)^2 / ln(p))
    public static long calculateMaxNumberOfElements(long bitSetSize, double falsePositiveProbability) {
        double numerator = bitSetSize * LN_2_SQUARED;
        double denominator = Math.log(falsePositiveProbability);
        return (long) -(numerator / denominator);
    }
}
