package io.github.vvcogo;

public interface HashingAlgorithm {
    long hash(final byte[] data, int length, long seed);
}
