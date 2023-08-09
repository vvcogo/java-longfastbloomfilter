package io.github.vvcogo;


import orestes.bloomfilter.FilterBuilder;

public class OrestesBloomFilterAdapter<T> implements BloomFilter<T>{

    private final BloomFilterConfiguration<? super T> config;
    private final orestes.bloomfilter.BloomFilter<T> bf;

    public OrestesBloomFilterAdapter(BloomFilterConfiguration<? super T> config){
        this.config = config;
        int expectedNumberOfElements = (int) config.expectedNumberOfElements();
        double falsePositiveRate = config.falsePositiveRate();
        bf = new FilterBuilder(expectedNumberOfElements, falsePositiveRate).buildBloomFilter();
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

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return null;
    }

}
