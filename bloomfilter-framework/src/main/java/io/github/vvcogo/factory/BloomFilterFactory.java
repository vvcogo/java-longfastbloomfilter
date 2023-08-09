package io.github.vvcogo.factory;

import io.github.vvcogo.bloomfilter.BloomFilter;
import io.github.vvcogo.BloomFilterConfiguration;

public interface BloomFilterFactory {

    <T> BloomFilter<T> create(BloomFilterConfiguration config);

    String getFactoryId();
}
