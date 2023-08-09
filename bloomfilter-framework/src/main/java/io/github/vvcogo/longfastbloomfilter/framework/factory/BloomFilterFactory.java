package io.github.vvcogo.longfastbloomfilter.framework.factory;

import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;

public interface BloomFilterFactory {

    <T> BloomFilter<T> create(BloomFilterConfiguration<T> config);

    String getFactoryId();
}
