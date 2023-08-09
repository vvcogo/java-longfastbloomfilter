package io.github.vvcogo;

import com.google.common.hash.Funnel;

public class GuavaBloomFilterAdapter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration<? super T> config;
    private final com.google.common.hash.BloomFilter<T> emptyBloomFilter;
    private com.google.common.hash.BloomFilter<T> bloomFilter;

    public GuavaBloomFilterAdapter(BloomFilterConfiguration<? super T> config) {
        this.config = config;
        this.emptyBloomFilter = createBloomFilter();
        this.bloomFilter = createBloomFilter();
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
        this.bloomFilter = createBloomFilter();
    }

    @Override
    public boolean isEmpty() {
        return this.bloomFilter.equals(this.emptyBloomFilter);
    }

    @Override
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return this.config;
    }

    private com.google.common.hash.BloomFilter<T> createBloomFilter() {
        long expected = config.expectedNumberOfElements();
        double fpp = config.falsePositiveRate();
        Funnel<? super T> funnel = (from, into) -> {
            into.putBytes(this.config.serializer().serialize(from));
        };
        return com.google.common.hash.BloomFilter.create(funnel, expected, fpp);
    }
}
