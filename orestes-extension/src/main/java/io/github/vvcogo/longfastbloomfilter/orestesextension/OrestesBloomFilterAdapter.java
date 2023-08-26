package io.github.vvcogo.longfastbloomfilter.orestesextension;


import io.github.vvcogo.longfastbloomfilter.framework.sus.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import orestes.bloomfilter.FilterBuilder;

public class OrestesBloomFilterAdapter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration<? super T> config;
    private orestes.bloomfilter.BloomFilter<T> bloomFilter;

    public OrestesBloomFilterAdapter(BloomFilterConfiguration<? super T> config){
        this.config = config;
        int expectedNumberOfElements = (int) this.config.expectedNumberOfElements();
        double falsePositiveRate = config.falsePositiveRate();
        this.bloomFilter = new FilterBuilder(expectedNumberOfElements, falsePositiveRate).buildBloomFilter();
    }

    @Override
    public void add(T element) {
        this.bloomFilter.add(element);
    }

    @Override
    public boolean mightContains(T element) {
        return this.bloomFilter.contains(element);
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
