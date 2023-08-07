package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashMurmur3;
import io.github.vvcogo.hashing.HashingAlgorithm;

public class HashMurmur3Factory implements HashingFactory {
    @Override
    public HashingAlgorithm create() {
        return new HashMurmur3();
    }
}
