package io.github.vvcogo.longfastbloomfilter.orestesextension;


import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;
import orestes.bloomfilter.FilterBuilder;

public class OrestesBloomFilterAdapter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration<? super T> config;
    private orestes.bloomfilter.BloomFilter<T> bloomFilter;
    private Serializer<? super T> serializer;

    public OrestesBloomFilterAdapter(BloomFilterConfiguration<? super T> config){
        this.config = config;
        this.serializer = this.config.serializer();
        int expectedNumberOfElements = (int) this.config.expectedNumberOfElements();
        double falsePositiveRate = config.falsePositiveRate();
        int bitSetSize = (int)this.config.bitSetSize();
        int numHashFuncs = this.config.numberOfHashFunctions();
        this.bloomFilter = new FilterBuilder(expectedNumberOfElements, falsePositiveRate)
                            .size(bitSetSize)
                            .hashes(numHashFuncs)
                            .hashFunction((b, m, k) -> {
                                int[] resultHashInt = new int[k];
                                long[] bytesHashLong = this.config.hashFunction().hash(b, k, m);
                                for(int i = 0; i < k; i++)
                                    resultHashInt[i] = (int)bytesHashLong[i];
                                return resultHashInt;
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
    public boolean isEmpty() {
        return this.bloomFilter.isEmpty();
    }

    @Override
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return this.config;
    }

    @Override
    public BloomFilter<T> copy() {
        OrestesBloomFilterAdapter<T> copy = new OrestesBloomFilterAdapter<>(this.config);
        copy.bloomFilter = this.bloomFilter.clone();
        return copy;
    }
}
