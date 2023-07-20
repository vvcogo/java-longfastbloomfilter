package longfastbloomfilter;

import java.math.BigInteger;

// TODO: thread-safety
public class BigIntegerBitSet {

    private static final int INTEGER_BIT_AMOUNT = 32;

    private BigInteger bitSet = BigInteger.ZERO;

    public void set(BigInteger bitIndex) {
        BigInteger bitMask = getBitMask(bitIndex);
        this.bitSet = this.bitSet.or(bitMask);
    }

    public void clear() {
        this.bitSet = BigInteger.ZERO;
    }

    public void clear(BigInteger bitIndex) {
        BigInteger bitMask = getBitMask(bitIndex).negate();
        this.bitSet = this.bitSet.and(bitMask);
    }

    public boolean get(BigInteger bitIndex) {
        BigInteger bitMask = getBitMask(bitIndex);
        BigInteger result = this.bitSet.and(bitMask);
        return !result.equals(BigInteger.ZERO);
    }

    private BigInteger getBitMask(BigInteger bitIndex) {
        BigInteger bitMask = BigInteger.ONE;
        while (!bitIndex.equals(BigInteger.ZERO)) {
            bitMask = bitMask.shiftLeft(bitIndex.intValue());
            bitIndex = bitIndex.shiftRight(INTEGER_BIT_AMOUNT);
        }
        return bitMask;
    }
}
