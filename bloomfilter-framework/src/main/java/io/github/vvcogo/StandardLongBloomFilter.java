package io.github.vvcogo;

public class StandardLongBloomFilter<T> extends AbstractBloomFilter<T, Long> {

    public StandardLongBloomFilter(BloomFilterConfiguration config) {
        super(new LongBitSet(config.getBitSetSize()));
    }
}
