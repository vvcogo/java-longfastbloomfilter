package io.github.vvcogo;

import io.github.vvcogo.bitset.LongBitSet;

public class StandardLongBloomFilter<T> extends AbstractBloomFilter<T, Long> {//FIXME: ver

    public StandardLongBloomFilter(BloomFilterConfiguration config) {
        super(new LongBitSet(config.getBitSetSize()));
    }
}
