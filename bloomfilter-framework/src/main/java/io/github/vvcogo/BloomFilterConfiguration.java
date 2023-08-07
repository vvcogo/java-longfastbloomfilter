package io.github.vvcogo;

public class BloomFilterConfiguration {

    private long bitSetSize;

    public BloomFilterConfiguration(long bitSetSize) {
        this.bitSetSize = bitSetSize;
    }

    public long getBitSetSize() {
        return this.bitSetSize;
    }
}
