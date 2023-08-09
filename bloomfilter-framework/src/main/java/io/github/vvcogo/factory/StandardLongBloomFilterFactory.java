package io.github.vvcogo.factory;

import io.github.vvcogo.bloomfilter.BloomFilter;
import io.github.vvcogo.BloomFilterConfiguration;
import io.github.vvcogo.bloomfilter.StandardLongBloomFilter;

public class StandardLongBloomFilterFactory implements BloomFilterFactory {

    @Override
    public <T> BloomFilter<T> create(BloomFilterConfiguration config) {
        return new StandardLongBloomFilter<>(config);
    }

    @Override
    public String getFactoryId() {
        return "longfastbloomfilter";
    }
}
