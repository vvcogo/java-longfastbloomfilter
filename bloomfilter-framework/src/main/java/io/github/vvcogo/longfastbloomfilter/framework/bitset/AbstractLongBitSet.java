package io.github.vvcogo.longfastbloomfilter.framework.bitset;

import java.util.Objects;

public abstract class AbstractLongBitSet implements BitSet {

    private static final long SINGLE_ARRAY_MAX_BIT_SIZE = Integer.MAX_VALUE * 64L;

    private final long size;

    protected AbstractLongBitSet(long size) {
        this.size = size;
        initializeBitArray(size);
    }

    @Override
    public void set(long index) {
        int arrayIndex = getArrayIndex(index);
        int elementIndex = getLongIndex(index);
        int bitIndex = getBitIndex(index);
        set(arrayIndex, elementIndex, bitIndex);
    }

    @Override
    public boolean get(long index) {
        int arrayIndex = getArrayIndex(index);
        int elementIndex = getLongIndex(index);
        int bitIndex = getBitIndex(index);
        return get(arrayIndex, elementIndex, bitIndex);
    }

    @Override
    public void clear() {
        initializeBitArray(this.size);
    }

    @Override
    public long getSize() {
        return this.size;
    }

    protected int getArrayIndex(long index) {
        return (int) (index / SINGLE_ARRAY_MAX_BIT_SIZE);
    }

    protected int getLongIndex(long index) {
        long remaining = index % SINGLE_ARRAY_MAX_BIT_SIZE;
        return (int) (remaining / 64);
    }

    protected int getBitIndex(long index) {
        return (int) (index % 64);
    }

    protected abstract void set(int arrayIndex, int elementIndex, int bitIndex);

    protected abstract boolean get(int arrayIndex, int elementIndex, int bitIndex);

    protected abstract void initializeBitArray(long size);

    @Override
    public final boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof AbstractLongBitSet) {
            AbstractLongBitSet other = (AbstractLongBitSet) obj;
            if (this.size == other.size) {
                for (long i = 0; i < getSize(); i++) {
                    if (get(i) != other.get(i))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.size);
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (long i = 0; i < getSize(); i++) {
            if (get(i)) {
                sb.append('1');
            } else {
                sb.append('0');
            }
        }
        return sb.append("]").toString();
    }
}
