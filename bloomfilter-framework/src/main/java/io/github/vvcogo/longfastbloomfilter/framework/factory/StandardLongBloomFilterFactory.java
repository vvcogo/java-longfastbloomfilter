package io.github.vvcogo.longfastbloomfilter.framework.factory;

import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.StandardLongBloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;

public class StandardLongBloomFilterFactory implements BloomFilterFactory {


    @Override
    public <T> BloomFilter<T> create(BloomFilterConfiguration<T> config) {
        return new StandardLongBloomFilter<>(config);
    }
}
