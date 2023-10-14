package io.github.vvcogo.longfastbloomfilter.framework.bitset;

public class LongBitSet extends AbstractLongBitSet {

    private long[][] bits;

    public LongBitSet(long size) {
        super(size);
    }

    @Override
    protected void set(int arrayIndex, int elementIndex, int bitIndex) {
        long bitMask = 1L << bitIndex;
        this.bits[arrayIndex][elementIndex] |= bitMask;
    }

    @Override
    protected boolean get(int arrayIndex, int elementIndex, int bitIndex) {
        long bitMask = 1L << bitIndex;
        long value = this.bits[arrayIndex][elementIndex];
        return (value & bitMask) != 0L;
    }

    @Override
    protected void initializeBitArray(long size) {
        int numberOfArrays = getArrayIndex(size) + 1;
        int lastArraySize = getLongIndex(size) + 1;
        this.bits = new long[numberOfArrays][];
        for (int i = 0; i < numberOfArrays - 1; i++) {
            this.bits[i] = new long[Integer.MAX_VALUE];
        }
        this.bits[numberOfArrays - 1] = new long[lastArraySize];
    }
}