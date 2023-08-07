package io.github.vvcogo.hashing;

public interface HashingAlgorithm {

    long[] hash(final byte[] msg, int k, long m);
}
