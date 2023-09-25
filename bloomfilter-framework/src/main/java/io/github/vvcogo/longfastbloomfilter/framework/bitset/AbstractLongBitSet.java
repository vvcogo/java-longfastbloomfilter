package io.github.vvcogo.longfastbloomfilter.framework.bitset;

import java.util.Objects;

public abstract class AbstractLongBitSet implements BitSet {

    private static final long SINGLE_ARRAY_MAX_BIT_SIZE = Integer.MAX_VALUE * 64L;

    private final long size;
    private final int numbOfArrays;
    private final int lastArraySize;
    private boolean empty = true;

    protected AbstractLongBitSet(long size) {
        this.size = size;
        long maxIndex = size - 1;
        this.numbOfArrays = getArrayIndex(maxIndex) + 1;
        this.lastArraySize = getElementIndex(maxIndex) + 1;
        initializeBitArray(numbOfArrays, lastArraySize);
    }

    @Override
    public void set(long index) {
        int arrayIndex = getArrayIndex(index);
        int elementIndex = getElementIndex(index);
        long bitIndex = getBitIndex(index);
        this.empty = false;
        set(arrayIndex, elementIndex, bitIndex);
    }

    @Override
    public boolean get(long index) {
        int arrayIndex = getArrayIndex(index);
        int elementIndex = getElementIndex(index);
        long bitIndex = getBitIndex(index);
        return get(arrayIndex, elementIndex, bitIndex);
    }

    @Override
    public void clear() {
        initializeBitArray(this.numbOfArrays, this.lastArraySize);
        this.empty = true;
    }

    @Override
    public boolean isEmpty() {
        return this.empty;
    }

    protected void setEmpty(boolean value) {
        this.empty = value;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    private int getArrayIndex(long index) {
        return (int) (index / SINGLE_ARRAY_MAX_BIT_SIZE);
    }

    private int getElementIndex(long index) {
        long remaining = index % SINGLE_ARRAY_MAX_BIT_SIZE;
        return (int) (remaining / 64);
    }

    private long getBitIndex(long index) {
        return index % 64L;
    }

    protected abstract void set(int arrayIndex, int elementIndex, long bitIndex);

    protected abstract boolean get(int arrayIndex, int elementIndex, long bitIndex);

    protected abstract void initializeBitArray(int numberOfArrays, int lastArraySize);

    @Override
    public final boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof AbstractLongBitSet) {
            AbstractLongBitSet other = (AbstractLongBitSet) obj;
            boolean equalAttributes = this.size == other.size &&
                                        this.numbOfArrays == other.numbOfArrays &&
                                        this.lastArraySize == other.lastArraySize &&
                                        this.empty == other.empty;
            if (equalAttributes) {
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
        return Objects.hash(this.size, this.numbOfArrays, this.lastArraySize, this.empty);
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
