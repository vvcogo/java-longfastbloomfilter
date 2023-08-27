package io.github.vvcogo.longfastbloomfilter.orestesextension;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterFactory;

public class OrestesBloomFilterFactory implements BloomFilterFactory {

    @Override
    public <T> BloomFilter<T> create(BloomFilterConfiguration<T> config) {
        return new OrestesBloomFilterAdapter<>(config);
    }
}
