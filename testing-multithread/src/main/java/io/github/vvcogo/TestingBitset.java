package io.github.vvcogo;

import io.github.vvcogo.longfastbloomfilter.framework.bitset.AbstractLongBitSet;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.BitSet;

public class TestingBitset extends AbstractLongBitSet {

    private long[][] bits;

    protected TestingBitset(long size) {
        super(size);
    }

    @Override
    protected void set(int arrayIndex, int elementIndex, long bitIndex) {
        long bitMask = 1L << bitIndex;
        this.bits[arrayIndex][elementIndex] |= bitMask;
    }

    @Override
    protected boolean get(int arrayIndex, int elementIndex, long bitIndex) {
        return false;
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
    public BitSet copy() {
        return null;
    }
}
