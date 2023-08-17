package io.github.vvcogo.longfastbloomfilter.framework.factory;


import io.github.vvcogo.longfastbloomfilter.framework.BloomFilterConfiguration;
import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;

public final class BloomFilterCreator {

    private BloomFilterCreator() {
        throw new UnsupportedOperationException("Cannot create instance of " + getClass().getName());
    }

    public static <T> BloomFilter<T> createBloomFilter(BloomFilterConfiguration<T> config){
        String bfType = config.bloomFilterType();
        return BloomFilterFactoryManager.getFactory(bfType).create(config);
    }

}
