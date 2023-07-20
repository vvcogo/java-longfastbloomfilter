package longfastbloomfilter;

import java.math.BigInteger;

public record BloomFilterConfiguration(BigInteger bitSetSize, BigInteger numberOfElements, int numberOfHashFunctions, double falsePositiveRate) {
    
}
