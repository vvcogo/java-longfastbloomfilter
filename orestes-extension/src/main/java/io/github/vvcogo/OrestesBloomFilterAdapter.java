package io.github.vvcogo;


import orestes.bloomfilter.FilterBuilder;

public class OrestesBloomFilterAdapter<T> implements BloomFilter<T>{

    private final BloomFilterConfiguration<? super T> config;
    private final orestes.bloomfilter.BloomFilter<T> orestesBloomfilter;

    public OrestesBloomFilterAdapter(BloomFilterConfiguration<? super T> config){
        this.config = config;
        int expectedNumberOfElements = (int) this.config.expectedNumberOfElements();
        double falsePositiveRate = config.falsePositiveRate();
        this.orestesBloomfilter = new FilterBuilder(expectedNumberOfElements, falsePositiveRate).buildBloomFilter();
    }

    @Override
    public void add(T element) {
        this.orestesBloomfilter.add(element);
    }

    @Override
    public boolean mightContains(T element) {
        return this.orestesBloomfilter.contains(element);
    }

    @Override
    public void clear() {
        this.orestesBloomfilter.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.orestesBloomfilter.isEmpty();
    }

    @Override
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return this.config;
    }

}
