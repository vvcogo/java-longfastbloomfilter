package longfastbloomfilter;

import java.math.BigInteger;
import java.util.Arrays;

// TODO: thread-safety
public class BigIntegerBitSet {

    private static final BigInteger MAX_INT_SIZE = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigInteger MAX_ARRAY_BITS = MAX_INT_SIZE.multiply(BigInteger.valueOf(64));

    private final long[][] bitSet;

    public BigIntegerBitSet(BigInteger size) {
        int numberOfArrays = size.divide(MAX_ARRAY_BITS).intValue() + 1;
        int remainderBits = size.mod(MAX_ARRAY_BITS).intValue();
        this.bitSet = new long[numberOfArrays][];
        for (int i = 0; i < numberOfArrays - 1; i++) {
            this.bitSet[i] = new long[Integer.MAX_VALUE];
        }
        double arraySizeDouble = remainderBits / 64.0;
        int arraySize = (int) Math.ceil(arraySizeDouble);
        this.bitSet[numberOfArrays - 1] = new long[arraySize];
    }

    public void set(BigInteger bitIndex) {
        int[] bitPositions = getBitPositions(bitIndex);
        int y = bitPositions[0];
        int x = bitPositions[1];
        this.bitSet[y][x] |= 1L << bitPositions[2];
    }

    public void clear() {
        for (long[] array : this.bitSet) {
            Arrays.fill(array, 0);
        }
    }

    public void clear(BigInteger bitIndex) {
        int[] bitPositions = getBitPositions(bitIndex);
        int y = bitPositions[0];
        int x = bitPositions[1];
        this.bitSet[y][x] &= ~1L << bitPositions[2];
    }

    public boolean get(BigInteger bitIndex) {
        int[] bitPositions = getBitPositions(bitIndex);
        int y = bitPositions[0];
        int x = bitPositions[1];
        long result = this.bitSet[y][x] & 1L << bitPositions[2];
        return result != 0;
    }

    private int[] getBitPositions(BigInteger bitIndex) {
        BigInteger[] divisionAndRemainder = bitIndex.divideAndRemainder(MAX_ARRAY_BITS);
        int arrayIndex = divisionAndRemainder[0].intValue();
        int offset = divisionAndRemainder[1].divide(BigInteger.valueOf(64)).intValue();
        int shift = divisionAndRemainder[1].mod(BigInteger.valueOf(64)).intValue();
        return new int[] {arrayIndex, offset, shift};
    }
}
