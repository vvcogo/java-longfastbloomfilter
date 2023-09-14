package io.github.vvcogo.longfastbloomfilter.framework.bloomfilter;

import io.github.vvcogo.longfastbloomfilter.framework.bitset.AtomicLongBitSet;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.BitSet;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;

import java.util.Objects;

public class StandardLongBloomFilter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration<? super T> configuration;
    private BitSet bitSet;

    public StandardLongBloomFilter(BloomFilterConfiguration<? super T> config) {
        this.configuration = config;
        this.bitSet = new AtomicLongBitSet(config.bitSetSize());
    }

    @Override
    public void add(T element) {
        byte[] bytes = toBytes(element);
        for (long hash : getHashes(bytes)) {
            this.bitSet.set(hash);
        }
    }

    @Override
    public boolean mightContains(T element) {
        byte[] bytes = toBytes(element);
        long[] hashes = getHashes(bytes);
        for (long hash : hashes) {
            if (!this.bitSet.get(hash))
                return false;
        }
        return true;
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
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return this.configuration;
    }

    @Override
    public BloomFilter<T> copy() {
        StandardLongBloomFilter<T> copy = new StandardLongBloomFilter<>(this.configuration);
        copy.bitSet = this.bitSet.copy();
        return copy;
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
        return String.format("%s%nConfiguration:%n%s%nBitSet:%n%s", getClass().getName(), this.configuration, this.bitSet);
    }

    private long[] getHashes(byte[] bytes) {
        long size = this.configuration.bitSetSize();
        int numbHashes = this.configuration.numberOfHashFunctions();
        return this.configuration.hashFunction().hash(bytes, numbHashes, size);
    }

    private byte[] toBytes(T element) {
        return this.configuration.serializer().serialize(element);
    }
}
