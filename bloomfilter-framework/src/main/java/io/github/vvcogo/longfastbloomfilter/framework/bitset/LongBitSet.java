package io.github.vvcogo.longfastbloomfilter.framework.bitset;

import java.util.Arrays;

public class LongBitSet extends AbstractLongBitSet {

    private long[][] bits;

    public LongBitSet(long size) {
        super(size);
    }

    @Override
    protected void set(int arrayIndex, int elementIndex, long bitIndex) {
        long bitMask = 1L << bitIndex;
        this.bits[arrayIndex][elementIndex] |= bitMask;
    }

    @Override
    protected boolean get(int arrayIndex, int elementIndex, long bitIndex) {
        long bitMask = 1L << bitIndex;
        long value = this.bits[arrayIndex][elementIndex];
        return (value & bitMask) != 0L;
    }

    @Override
    protected void initializeBitArray(int numberOfArrays, int lastArraySize) {
        this.bits = new long[numberOfArrays][];
        for (int i = 0; i < numberOfArrays - 1; i++) {
            this.bits[i] = new long[Integer.MAX_VALUE];
        }
        this.bits[numberOfArrays - 1] = new long[lastArraySize];
    }

    @Override
    public LongBitSet copy() {
        LongBitSet copy = new LongBitSet(getSize());
        for (int i = 0; i < this.bits.length; i++) {
            copy.bits[i] = Arrays.copyOf(this.bits[i], this.bits[i].length);
        }
        copy.setEmpty(isEmpty());
        return copy;
    }
}
