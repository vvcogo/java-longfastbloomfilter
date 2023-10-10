package io.github.vvcogo.longfastbloomfilter.framework.bitset;

import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicLongBitSet extends AbstractLongBitSet {

    private AtomicLongArray[] bits;

    public AtomicLongBitSet(long size) {
        super(size);
    }

    @Override
    protected void set(int arrayIndex, int elementIndex, long bitIndex) {
        long bitMask = 1L << bitIndex;
        long value;
        do {
            value = this.bits[arrayIndex].get(elementIndex);
            if ((value & bitMask) != 0L)
                return;
        } while(!this.bits[arrayIndex].compareAndSet(elementIndex, value, value | bitMask));
    }

    @Override
    protected boolean get(int arrayIndex, int elementIndex, long bitIndex) {
        long bitMask = 1L << bitIndex;
        long value = this.bits[arrayIndex].get(elementIndex);
        return (value & bitMask) != 0L;
    }

    @Override
    public AtomicLongBitSet copy() {
        AtomicLongBitSet copy = new AtomicLongBitSet(getSize());
        for (int i = 0; i < this.bits.length; i++) {
            for (int j = 0; j < this.bits[i].length(); j++) {
                copy.bits[i].set(j, this.bits[i].get(j));
            }
        }
        copy.setEmpty(isEmpty());
        return copy;
    }

    @Override
    protected void initializeBitArray(int numberOfArrays, int lastArraySize) {
        this.bits = new AtomicLongArray[numberOfArrays];
        for (int i = 0; i < numberOfArrays - 1; i++) {
            this.bits[i] = new AtomicLongArray(Integer.MAX_VALUE);
        }
        this.bits[numberOfArrays - 1] = new AtomicLongArray(lastArraySize);
    }
}
