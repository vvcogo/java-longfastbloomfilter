package io.github.vvcogo.longfastbloomfilter.guavaextension;

import com.google.common.hash.BloomFilterGuavaCreate;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;

public class GuavaBloomFilterAdapter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration<? super T> config;
    private com.google.common.hash.BloomFilter<T> bloomFilter;
    private boolean isEmpty = false;

    public GuavaBloomFilterAdapter(BloomFilterConfiguration<? super T> config) {
        this.config = config;
        this.bloomFilter = BloomFilterGuavaCreate.createBloomFilter(this.config);
    }

    private GuavaBloomFilterAdapter(GuavaBloomFilterAdapter other){
        this.config = other.config;
        this.isEmpty = other.isEmpty;
        this.bloomFilter = other.bloomFilter.copy();
    }

    @Override
    public void add(T element) {
        this.bloomFilter.put(element);
        this.isEmpty = false;
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
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return this.config;
    }
}
