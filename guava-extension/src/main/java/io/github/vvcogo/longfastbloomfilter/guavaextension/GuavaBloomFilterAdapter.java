package io.github.vvcogo.longfastbloomfilter.guavaextension;

import com.google.common.hash.BloomFilterGuavaCreate;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;

public class GuavaBloomFilterAdapter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration<? super T> config;
    private final com.google.common.hash.BloomFilter<T> emptyBloomFilter;
    private com.google.common.hash.BloomFilter<T> bloomFilter;

    public GuavaBloomFilterAdapter(BloomFilterConfiguration<? super T> config) {
        this.config = config;
        this.emptyBloomFilter = BloomFilterGuavaCreate.createBloomFilter(this.config);
        this.bloomFilter = BloomFilterGuavaCreate.createBloomFilter(this.config);
    }

    @Override
    public void add(T element) {
        this.bloomFilter.put(element);
    }

    @Override
    public boolean mightContains(T element) {
        return this.bloomFilter.mightContain(element);
    }

    @Override
    public void clear() {
        this.bloomFilter = BloomFilterGuavaCreate.createBloomFilter(this.config);
    }

    @Override
    public boolean isEmpty() {
        return this.bloomFilter.equals(this.emptyBloomFilter);
    }

    @Override
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return this.config;
    }

    @Override
    public BloomFilter<T> copy() {
        GuavaBloomFilterAdapter<T> copy = new GuavaBloomFilterAdapter<>(this.config);
        copy.bloomFilter = this.bloomFilter.copy();
        return copy;
    }
}
