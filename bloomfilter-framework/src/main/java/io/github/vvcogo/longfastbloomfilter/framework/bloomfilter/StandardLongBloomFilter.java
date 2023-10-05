package io.github.vvcogo.longfastbloomfilter.framework.bloomfilter;

import io.github.vvcogo.longfastbloomfilter.framework.bitset.AtomicLongBitSet;
import io.github.vvcogo.longfastbloomfilter.framework.bitset.BitSet;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;

import java.nio.ByteBuffer;
import java.util.Objects;

public class StandardLongBloomFilter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration<? super T> configuration;
    private BitSet bitSet;

    public StandardLongBloomFilter(BloomFilterConfiguration<? super T> config) {
        this.configuration = config;
        this.bitSet = new AtomicLongBitSet(config.getBitSetSize());
    }

    @Override
    public void add(T element) {
        byte[] bytes = toBytes(element);
        ByteBuffer buffer = getHashes(bytes);
        while (buffer.remaining() > 7) {
            long index = buffer.getLong();
            this.bitSet.set(index);
        }
    }

    @Override
    public boolean mightContains(T element) {
        byte[] bytes = toBytes(element);
        ByteBuffer buffer = getHashes(bytes);
        while (buffer.remaining() > 7) {
            long index = buffer.getLong();
            if (!this.bitSet.get(index))
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
        if (obj instanceof StandardLongBloomFilter<?>) {
            StandardLongBloomFilter<?> other = (StandardLongBloomFilter<?>) obj;
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

    private ByteBuffer getHashes(byte[] bytes) {
        long size = this.configuration.getBitSetSize();
        int numbHashes = this.configuration.getNumberOfHashFunctions();
        return this.configuration.getHashFunction().hash(bytes, numbHashes, size);
    }

    private byte[] toBytes(T element) {
        return this.configuration.getSerializer().serialize(element);
    }
}
