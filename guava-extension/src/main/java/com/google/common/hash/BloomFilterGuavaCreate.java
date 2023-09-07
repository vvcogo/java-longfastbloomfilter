package com.google.common.hash;


import com.google.common.primitives.Longs;
import io.github.vvcogo.longfastbloomfilter.framework.configuration.BloomFilterConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class BloomFilterGuavaCreate {

    private BloomFilterGuavaCreate() {
        throw new UnsupportedOperationException("Can not create an instance of " + getClass().getName());
    }

    static class BloomFilterStrategy implements BloomFilter.Strategy {

        @Override
        public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.LockFreeBitArray bits) {
            long[] indices = getIndices(object, funnel, numHashFunctions, bits.bitSize());
            boolean changed = false;
            for (long index : indices) {
                if (bits.set(index)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override
        public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.LockFreeBitArray bits) {
            long[] indices = getIndices(object, funnel, numHashFunctions, bits.bitSize());
            for (long index : indices) {
                if (!bits.get(index)) {
                    return false;
                }
            }
            return true;
        }

        private <T> long[] getIndices(T object, Funnel<? super T> funnel, int numbOfHashFunctions, long bitSize) {
            BloomFilterHasher hasher = new BloomFilterHasher(numbOfHashFunctions);
            HashCode hashCode = hasher.putObject(object, funnel).hash();
            byte[] bytes = hashCode.getBytesInternal();
            long[] indices = new long[numbOfHashFunctions];
            for (int i = 0; i < bytes.length; i += 8) {
                long value = Longs.fromBytes(bytes[i], bytes[i+1], bytes[i+2], bytes[i+3], bytes[i+4], bytes[i+5], bytes[i+6], bytes[i+7]);
                indices[i/8] = (value & Long.MAX_VALUE) % bitSize;
            }
            return indices;
        }

        @Override
        public int ordinal() {
            return -111;
        }
    }

    static class BloomFilterHasher extends AbstractHasher {

        private final ByteBuffer buffer;

        public BloomFilterHasher(int numbOfHashFunctions) {
            this.buffer = ByteBuffer.allocate(numbOfHashFunctions * Long.BYTES);
        }

        @Override
        public Hasher putByte(byte b) {
            buffer.put(b);
            return this;
        }

        @Override
        public HashCode hash() {
            return HashCode.fromBytesNoCopy(buffer.order(ByteOrder.LITTLE_ENDIAN).array());
        }
    }

    public static <T> BloomFilter<T> createBloomFilter(BloomFilterConfiguration<? super T> config) {
        int numHashFunc = config.numberOfHashFunctions();
        Funnel<? super T> funnel = (from, into) -> into.putBytes(config.serializer().serialize(from));
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
