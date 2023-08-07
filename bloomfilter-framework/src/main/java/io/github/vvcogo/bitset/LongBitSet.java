package io.github.vvcogo.bitset;

import java.util.Arrays;

public class LongBitSet implements BitSet<Long> {

    private static final long SINGLE_ARRAY_MAX_BIT_SIZE = Integer.MAX_VALUE * 64L;

    private final long[][] bits;

    public LongBitSet(long size) {
        long maxIndex = size - 1;
        int numbOfArrays = getArrayIndex(maxIndex) + 1;
        this.bits = new long[numbOfArrays][];
        for (int i = 0; i < numbOfArrays - 1; i++) {
            this.bits[i] = new long[Integer.MAX_VALUE];
        }
        int lastArraySize = getElementIndex(maxIndex) + 1;
        this.bits[numbOfArrays - 1] = new long[lastArraySize];
    }

    @Override
    public void set(Long index) {
        int arrayIndex = getArrayIndex(index);
        int elementIndex = getElementIndex(index);
        this.bits[arrayIndex][elementIndex] |= 1L << elementIndex;
    }

    @Override
    public boolean get(Long index) {
        int arrayIndex = getArrayIndex(index);
        int elementIndex = getElementIndex(index);
        long bitMask = 1L << elementIndex;
        return (this.bits[arrayIndex][elementIndex] & bitMask) != 0;
    }

    @Override
    public void clear() {
        for (long[] array : this.bits) {
            Arrays.fill(array, 0);
        }
    }

    private int getArrayIndex(long index) {
        return (int) (index / SINGLE_ARRAY_MAX_BIT_SIZE);
    }

    private int getElementIndex(long index) {
        long remaining = index % SINGLE_ARRAY_MAX_BIT_SIZE;
        return (int) (remaining / 64);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (long[] array : this.bits) {
            sb.append(Arrays.toString(array)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj != null && getClass() == obj.getClass()) {
            LongBitSet other = (LongBitSet) obj;
            if (this.bits.length != other.bits.length)
                return false;
            for (int i = 0; i < this.bits.length; i++) {
                if (!Arrays.equals(this.bits[i], other.bits[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (long[] array : this.bits) {
            hash += Arrays.hashCode(array);
        }
        return hash;
    }
}
