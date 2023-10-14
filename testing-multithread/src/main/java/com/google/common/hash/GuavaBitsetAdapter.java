package com.google.common.hash;

import io.github.vvcogo.longfastbloomfilter.framework.bitset.BitSet;

public class GuavaBitsetAdapter implements BitSet {

    private final BloomFilterStrategies.LockFreeBitArray bitArray;

    public GuavaBitsetAdapter(long size) {
        this.bitArray = new BloomFilterStrategies.LockFreeBitArray(size);
    }

    @Override
    public void set(long index) {
        this.bitArray.set(index);
    }

    @Override
    public boolean get(long index) {
        return this.bitArray.get(index);
    }

    @Override
    public void clear() {

    }

    @Override
    public long getSize() {
        return 0;
    }
}
