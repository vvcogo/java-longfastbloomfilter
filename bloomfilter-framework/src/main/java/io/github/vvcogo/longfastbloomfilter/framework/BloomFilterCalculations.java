package io.github.vvcogo.longfastbloomfilter.framework;


public final class BloomFilterCalculations {

    private static final double LN_2 = Math.log(2);
    private static final double LN_2_SQUARED = Math.pow(LN_2, 2);


    private BloomFilterCalculations() {
        throw new UnsupportedOperationException("Cannot created instance of " + getClass().getName());
    }

    // m = -(n * ln(p) / ln(2) ^ 2)
    public static long calculateMinBitSetSize(long expectedNumberOfElements, double falsePositiveProbability) {
        return (long) (-expectedNumberOfElements * Math.log(falsePositiveProbability) / LN_2_SQUARED);
    }

    // k = ln(2) * m/n
    public static int calculateOptimalNumberOfHashFunctions(long bitSetSize, long expectedNumberOfElements) {
        return (int) (LN_2 * bitSetSize / expectedNumberOfElements);
    }

    // n = -(m * ln(2)^2 / ln(p))
    public static long calculateMaxNumberOfElements(long bitSetSize, double falsePositiveProbability) {
        return (long) (-bitSetSize * LN_2_SQUARED / Math.log(falsePositiveProbability));
    }

    // n = (ln(2) * m) / k
    public static long calculateMaxNumberOfElements(long bitSetSize, int numberOfHashFuncs) {
        return (long) (LN_2 * bitSetSize) / numberOfHashFuncs;
    }

    // p = e ^ ((-ln(2)^2 * m)/n)
    public static double calculateFalsePositiveProbability(long bitSetSize, long expectedNumberOfElements) {
        return Math.pow(Math.E, (- LN_2_SQUARED * bitSetSize) / expectedNumberOfElements);
    }
}
