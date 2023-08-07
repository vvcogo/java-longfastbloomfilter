package io.github.vvcogo;

import io.github.vvcogo.bitset.BitSet;

import java.util.Collection;
import java.util.Objects;

public abstract class AbstractBloomFilter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration configuration;
    private final BitSet bitSet;

    protected AbstractBloomFilter(BloomFilterConfiguration configuration, BitSet bitSet) {
        this.configuration = configuration;
        this.bitSet = bitSet;
    }

    @Override
    public void add(T element) {
        // TODO
    }

    @Override
    public void addAll(Collection<T> elements) {
        for (T element : elements) {
            add(element);
        }
    }

    @Override
    public boolean mightContains(T element) {
        return false;
    }

    @Override
    public boolean[] mightContains(Collection<T> elements) {
        boolean[] result = new boolean[elements.size()];
        int i = 0;
        for (T element : elements) {
            result[i] = mightContains(element);
            i++;
        }
        return result;
    }

    @Override
    public void clear() {
        this.bitSet.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.bitSet.isEmpty();
    }

    @Override
    public BloomFilterConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public boolean isCompatible(BloomFilter<T> other) {
        return false;
    }

    @Override
    public void union(BloomFilter<T> other) {

    }

    @Override
    public void intersect(BloomFilter<T> other) {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj != null && getClass() == obj.getClass()) {
            AbstractBloomFilter<T> other = (AbstractBloomFilter<T>) obj;
            return this.configuration.equals(other.configuration) && this.bitSet.equals(other.bitSet);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(configuration, bitSet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName())
                .append("\nConfiguration:\n")
                .append(this.configuration.toString())
                .append("\nBitSet:\b")
                .append(this.bitSet.toString());
        return sb.toString();
    }
}
