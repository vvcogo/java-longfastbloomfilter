package io.github.vvcogo.hashing;

public interface HashingAlgorithm {

    long[] hash(byte[] msg, int k, long m);
}
