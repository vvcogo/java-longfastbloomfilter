package com.google.common.hash;

import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class BloomFilterGuavaCreate {

    private BloomFilterGuavaCreate() {
        throw new UnsupportedOperationException("Can not create an instance of " + getClass().getName());
    }

    public static <T> BloomFilter<T> createBloomFilter(BloomFilterConfiguration<? super T> config) {
        int numHashFunc = config.getNumberOfHashFunctions();
        Funnel<? super T> funnel = (from, into) -> into.putBytes(config.getSerializer().serialize(from));
        BloomFilterStrategies.LockFreeBitArray lockFreeBitArray = new BloomFilterStrategies.LockFreeBitArray(config.getBitSetSize());
        BloomFilter.Strategy strategy = BloomFilterStrategies.MURMUR128_MITZ_64;
        try {
            Constructor<?> constructor = BloomFilter.class.getDeclaredConstructor(BloomFilterStrategies.LockFreeBitArray.class, int.class, Funnel.class, BloomFilter.Strategy.class);
            constructor.setAccessible(true);
            return (BloomFilter<T>) constructor.newInstance(lockFreeBitArray, numHashFunc, funnel, strategy);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
