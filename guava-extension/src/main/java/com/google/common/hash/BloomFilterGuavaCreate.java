package com.google.common.hash;


import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

public class BloomFilterGuavaCreate {

    private static class StrategyBF implements BloomFilter.Strategy {

        @Override
        public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.LockFreeBitArray bits){
            long[] indexes = getIndexes(object, funnel, numHashFunctions);
            boolean result = true;
            for(long index : indexes)
                result = result && bits.set(index);
            return result;
        }

        @Override
        public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.LockFreeBitArray bits){
            long[] indexes = getIndexes(object, funnel, numHashFunctions);
            boolean result = true;
            for(long index : indexes)
                result = result && bits.get(index);
            return result;
        }

        private <T> long[] getIndexes(T elem, Funnel<? super T> funnel, int numHashFunctions){
            PrimitiveSinkBF sink = new PrimitiveSinkBF(numHashFunctions);
            funnel.funnel(elem, sink);
            HashCode hashcode = sink.hash();
            ByteBuffer buffer = ByteBuffer.wrap(hashcode.asBytes());
            return buffer.asLongBuffer().array();
        }

        @Override
        public int ordinal() {
            return -111;
        }
    }

    private static class PrimitiveSinkBF extends AbstractHasher{

        private ByteBuffer buffer;

        public PrimitiveSinkBF(int numHashFuncs) {
            this.buffer = ByteBuffer.allocate(numHashFuncs * 8);
        }

        @Override
        public Hasher putByte(byte b) {
            buffer.put(b);
            return this;
        }

        @Override
        public HashCode hash() {
            return HashCode.fromBytesNoCopy(buffer.array());
        }
    }

    public static <T> BloomFilter<T> createBloomFilter(BloomFilterConfiguration<? super T> config) {
        long expected = config.expectedNumberOfElements();
        double fpp = config.falsePositiveRate();
        int numHashFunc = config.numberOfHashFunctions();
        Funnel<? super T> funnel = (from, into) -> {
            into.putBytes(config.serializer().serialize(from));
        };
        BloomFilterStrategies.LockFreeBitArray lockFreeBitArray = new BloomFilterStrategies.LockFreeBitArray(config.bitSetSize());
        BloomFilter.Strategy strategy = new StrategyBF();
        try {
            Constructor<?> constructor = BloomFilter.class.getDeclaredConstructor(BloomFilterStrategies.LockFreeBitArray.class, Integer.class, Funnel.class, BloomFilter.Strategy.class);
            constructor.setAccessible(true);
            BloomFilter<T> bf = (BloomFilter<T>) constructor.newInstance(lockFreeBitArray, numHashFunc, funnel, strategy);
            return bf;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
