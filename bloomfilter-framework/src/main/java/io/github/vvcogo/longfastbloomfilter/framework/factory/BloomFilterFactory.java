package io.github.vvcogo.longfastbloomfilter.framework.factory;

import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;

public interface BloomFilterFactory {

    <T> BloomFilter<T> create(BloomFilterConfiguration<T> config);
}
