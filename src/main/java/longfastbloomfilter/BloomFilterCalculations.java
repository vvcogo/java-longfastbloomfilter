package longfastbloomfilter;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class BloomFilterCalculations {

    private static final double OPTIMAL_NUMBER_OF_HASH_FUNCTIONS_MULTIPLIER = Math.log(2);
    private static final double ARRAY_SIZE_DENOMINATOR = Math.pow(OPTIMAL_NUMBER_OF_HASH_FUNCTIONS_MULTIPLIER, 2);

    // m = (-n * ln(p))/ln(2)^2
    // FIXME: RoundingMode in divide?
    public static BigInteger calculateMinBitArraySize(BigInteger numberOfElements, double falsePositiveProbability) {
        double logP = Math.log(falsePositiveProbability);
        BigDecimal numerator = new BigDecimal(numberOfElements.negate()).multiply(BigDecimal.valueOf(logP));
        BigDecimal denominator = BigDecimal.valueOf(ARRAY_SIZE_DENOMINATOR);
        return numerator.divide(denominator).toBigInteger();
    }

    // k = ln(2) * m/n
    // FIXME: RoundingMode in divide?
    public static int optimalNumberOfHashFunctions(BigInteger bitSetSize, BigInteger numberOfElements) {
        BigDecimal numerator = new BigDecimal(bitSetSize);
        BigDecimal denominator = new BigDecimal(numberOfElements);
        BigDecimal multiplier = BigDecimal.valueOf(OPTIMAL_NUMBER_OF_HASH_FUNCTIONS_MULTIPLIER);
        BigDecimal result = multiplier.multiply(numerator.divide(denominator));
        return result.toBigInteger().intValue();
    }
}
