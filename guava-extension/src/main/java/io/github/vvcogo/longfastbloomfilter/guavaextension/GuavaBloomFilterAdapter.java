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

    private GuavaBloomFilterAdapter(BloomFilterConfiguration<? super T> config, boolean isEmpty){
        this.config = config;
        this.isEmpty = isEmpty;
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
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return this.config;
    }

    @Override
    public BloomFilter<T> copy() {
        GuavaBloomFilterAdapter<T> copy = new GuavaBloomFilterAdapter<>(this.config, this.isEmpty);
        copy.bloomFilter = this.bloomFilter.copy();
        return copy;
    }
}
