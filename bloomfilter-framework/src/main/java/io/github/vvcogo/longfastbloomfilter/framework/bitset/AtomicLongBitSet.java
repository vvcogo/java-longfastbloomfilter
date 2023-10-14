package io.github.vvcogo.longfastbloomfilter.framework.bitset;

import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicLongBitSet extends AbstractLongBitSet {

    private AtomicLongArray[] bits;

    public AtomicLongBitSet(long size) {
        super(size);
    }

    @Override
    protected void set(int arrayIndex, int elementIndex, int bitIndex) {
        long bitMask = 1L << bitIndex;
        long value;
        do {
            value = this.bits[arrayIndex].get(elementIndex);
            if ((value & bitMask) != 0L)
                return;
        } while(!this.bits[arrayIndex].compareAndSet(elementIndex, value, value | bitMask));
    }

    @Override
    protected boolean get(int arrayIndex, int elementIndex, int bitIndex) {
        long bitMask = 1L << bitIndex;
        long value = this.bits[arrayIndex].get(elementIndex);
        return (value & bitMask) != 0L;
    }

    @Override
    protected void initializeBitArray(long size) {
        int numberOfArrays = getArrayIndex(size) + 1;
        int lastArraySize = getLongIndex(size) + 1;
        this.bits = new AtomicLongArray[numberOfArrays];
        for (int i = 0; i < numberOfArrays - 1; i++) {
            this.bits[i] = new AtomicLongArray(Integer.MAX_VALUE);
        }
        this.bits[numberOfArrays - 1] = new AtomicLongArray(lastArraySize);
    }
}
