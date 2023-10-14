package io.github.vvcogo.longfastbloomfilter.orestesextension;


import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;
import orestes.bloomfilter.FilterBuilder;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class OrestesBloomFilterAdapter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration<? super T> config;
    private orestes.bloomfilter.BloomFilter<T> bloomFilter;
    private Serializer<? super T> serializer;

    public OrestesBloomFilterAdapter(BloomFilterConfiguration<? super T> config){
        this.config = config;
        this.serializer = this.config.getSerializer();
        int expectedNumberOfElements = (int) this.config.getExpectedNumberOfElements();
        double falsePositiveRate = config.getFalsePositiveRate();
        int bitSetSize = (int)this.config.getBitSetSize();
        int numHashFuncs = this.config.getNumberOfHashFunctions();
        this.bloomFilter = new FilterBuilder(expectedNumberOfElements, falsePositiveRate)
                            .size(bitSetSize)
                            .hashes(numHashFuncs)
                            .hashFunction((b, m, k) -> {
                                ByteBuffer buffer = ByteBuffer.allocate(k * Integer.BYTES);
                                IntBuffer intBuffer = buffer.asIntBuffer();
                                ByteBuffer hashesBuffer = this.config.getHashFunction().hash(b, k, m);
                                while (hashesBuffer.remaining() > 7) {
                                    long value = hashesBuffer.getLong();
                                    int index = (int) value;
                                    intBuffer.put((index & Integer.MAX_VALUE) % m);
                                }
                                return intBuffer.array();
                            })
                            .buildBloomFilter();
    }

    @Override
    public void add(T element) {
        this.bloomFilter.addRaw(this.serializer.serialize(element));
    }

    @Override
    public boolean mightContains(T element) {
        return this.bloomFilter.contains(this.serializer.serialize(element));
    }

    @Override
    public void clear() {
        this.bloomFilter.clear();
    }

    @Override
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return this.config;
    }
}
