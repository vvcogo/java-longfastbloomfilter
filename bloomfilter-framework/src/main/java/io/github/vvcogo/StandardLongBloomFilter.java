package io.github.vvcogo;

import io.github.vvcogo.bitset.BitSet;
import io.github.vvcogo.bitset.LongBitSet;

import java.util.Objects;

public class StandardLongBloomFilter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration configuration;
    private final BitSet bitSet;

    public StandardLongBloomFilter(BloomFilterConfiguration config) {
        this.configuration = config;
        this.bitSet = new LongBitSet(config.bitSetSize());
    }

    @Override
    public void add(T element) {
    }

    @Override
    public boolean mightContains(T element) {
        return false;
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


    //TODO
    @Override
    public boolean isCompatible(BloomFilter<T> other) {
        return false;
    }

    @Override
    public void union(BloomFilter<T> other) {
        if (!isCompatible(other)) {
            throw new RuntimeException("Incompatible BloomFilters!");
        }

    }

    @Override
    public void intersect(BloomFilter<T> other) {
        if (!isCompatible(other)) {
            throw new RuntimeException("Incompatible BloomFilters!");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof StandardLongBloomFilter<?> other) {
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

    private void add(byte[] bytes) {

    }
}
