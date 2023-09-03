package com.google.common.hash;


import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;

public class BloomFilterGuavaCreate {

    public static <T> BloomFilter<T> createBloomFilter(BloomFilterConfiguration<? super T> config) {
        long expected = config.expectedNumberOfElements();
        double fpp = config.falsePositiveRate();
        int numHashFunc = config.numberOfHashFunctions();
        Funnel<? super T> funnel = (from, into) -> {
            into.putBytes(config.serializer().serialize(from));
        };
        BloomFilterStrategies.LockFreeBitArray lockFreeBitArray = new BloomFilterStrategies.LockFreeBitArray(config.bitSetSize());
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
