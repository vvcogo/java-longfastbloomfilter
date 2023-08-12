package io.github.vvcogo.longfastbloomfilter.framework.bitset;


public abstract class AbstractLongBitSet implements BitSet {

    private static final long SINGLE_ARRAY_MAX_BIT_SIZE = Integer.MAX_VALUE * 64L;

    private final long size;
    private final int numbOfArrays;
    private final int lastArraySize;
    private boolean empty = false;

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
        this.empty = false;
        set(arrayIndex, elementIndex);
    }

    @Override
    public boolean get(long index) {
        int arrayIndex = getArrayIndex(index);
        int elementIndex = getElementIndex(index);
        return get(arrayIndex, elementIndex);
    }

    @Override
    public void clear() {
        initializeBitArray(this.numbOfArrays, this.lastArraySize);
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

    protected abstract void set(int arrayIndex, int elementIndex);

    protected abstract boolean get(int arrayIndex, int elementIndex);

    protected abstract void initializeBitArray(int numberOfArrays, int lastArraySize);
}
