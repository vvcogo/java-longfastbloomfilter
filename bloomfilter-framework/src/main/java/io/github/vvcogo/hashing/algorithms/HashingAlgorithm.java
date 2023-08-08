package io.github.vvcogo.hashing.algorithms;

public interface HashingAlgorithm {

    long[] hash(byte[] msg, int k, long m);
}
