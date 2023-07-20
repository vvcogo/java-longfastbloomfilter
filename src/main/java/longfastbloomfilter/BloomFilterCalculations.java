package longfastbloomfilter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public final class BloomFilterCalculations {

    private static final double OPTIMAL_NUMBER_OF_HASH_FUNCTIONS_MULTIPLIER = Math.log(2);
    private static final double ARRAY_SIZE_DENOMINATOR = Math.pow(OPTIMAL_NUMBER_OF_HASH_FUNCTIONS_MULTIPLIER, 2);

    private BloomFilterCalculations() { }

    // m = (-n * ln(p))/ln(2)^2
    public static BigInteger calculateMinBitArraySize(BigInteger numberOfElements, double falsePositiveProbability) {
        double logP = Math.log(falsePositiveProbability);
        BigDecimal numerator = new BigDecimal(numberOfElements.negate()).multiply(BigDecimal.valueOf(logP));
        BigDecimal denominator = BigDecimal.valueOf(ARRAY_SIZE_DENOMINATOR);
        return numerator.divide(denominator, RoundingMode.FLOOR).toBigInteger();
    }

    // k = ln(2) * m/n
    public static int optimalNumberOfHashFunctions(BigInteger bitSetSize, BigInteger numberOfElements) {
        BigDecimal numerator = new BigDecimal(bitSetSize);
        BigDecimal denominator = new BigDecimal(numberOfElements);
        BigDecimal multiplier = BigDecimal.valueOf(OPTIMAL_NUMBER_OF_HASH_FUNCTIONS_MULTIPLIER);
        BigDecimal result = multiplier.multiply(numerator.divide(denominator, RoundingMode.FLOOR));
        return result.toBigInteger().intValue();
    }
}
