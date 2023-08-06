package io.github.vvcogo;

public class StandardLongBloomFilter<T> extends AbstractBloomFilter<T, Long> {

    public StandardLongBloomFilter(long size) {
        super(new LongBitSet(size));
    }
}
