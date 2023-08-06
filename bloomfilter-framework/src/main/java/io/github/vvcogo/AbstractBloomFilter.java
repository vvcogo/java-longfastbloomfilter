package io.github.vvcogo;

import java.util.Collection;

public abstract class AbstractBloomFilter<T, U extends Number> implements BloomFilter<T> {

    private final BitSet<U> bitSet;

    protected AbstractBloomFilter(BitSet<U> bitSet) {
        this.bitSet = bitSet;
    }

    @Override
    public void add(T elem) {
    }

    @Override
    public void addAll(Collection<T> collection) {
        for (T elem : collection) {
            add(elem);
        }
    }

    @Override
    public boolean mightContains(T elem) {
        return false;
    }

    @Override
    public boolean[] mightContains(Collection<T> elems) {
        boolean[] result = new boolean[elems.size()];
        int i = 0;
        for (T elem : elems) {
            result[i] = mightContains(elem);
            i++;
        }
        return result;
    }

    @Override
    public void clear() {
        this.bitSet.clear();
    }
}
