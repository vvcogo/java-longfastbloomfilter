package longfastbloomfilter;

import java.math.BigInteger;
import java.util.Collection;
import java.util.function.Function;

public class StandardBloomFilter<T> implements BloomFilter<T> {

    private final BloomFilterConfiguration configuration;
    private final BigIntegerBitSet bitSet = new BigIntegerBitSet();
    private final IHash hashingFunction;
    private final Function<T, byte[]> serializer;

    public StandardBloomFilter(BloomFilterConfiguration configuration, IHash hashingFunction, Function<T, byte[]> serializer) {
        this.configuration = configuration;
        this.hashingFunction = hashingFunction;
        this.serializer = serializer;
    }

    @Override
    public void add(T element) {
        BigInteger[] indices = getBitSetIndices(element);
        for (BigInteger index : indices) {
            this.bitSet.set(index);
        }
    }

    @Override
    public void addAll(Collection<T> elements) {
        for (T element : elements) {
            add(element);
        }
    }

    @Override
    public boolean mightContains(T element) {
        BigInteger[] indices = getBitSetIndices(element);
        for (BigInteger index : indices) {
            if (!this.bitSet.get(index)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        this.bitSet.clear();
    }

    @Override
    public BloomFilterConfiguration getConfiguration() {
        return this.configuration;
    }

    private BigInteger[] getBitSetIndices(T element) {
        BigInteger[] indices = new BigInteger[this.configuration.numberOfHashFunctions()];
        byte[] bytes = this.serializer.apply(element);
        long hash1 = this.hashingFunction.hash(bytes, bytes.length, 0);
        long hash2 = this.hashingFunction.hash(bytes, bytes.length, hash1);
        BigInteger bigHash1 = BigInteger.valueOf(hash1);
        BigInteger bigHash2 = BigInteger.valueOf(hash2);
        for (int i = 0; i < this.configuration.numberOfHashFunctions(); i++) {
            BigInteger bigI = BigInteger.valueOf(i);
            BigInteger index = bigHash1.add(bigHash2.multiply(bigI)).abs();
            indices[i] = index;
        }
        return indices;
    }
}
