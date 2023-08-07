package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashSHA256;
import io.github.vvcogo.hashing.HashingAlgorithm;

public class HashSHA256Factory implements HashingFactory{
    @Override
    public HashingAlgorithm create() {
        return new HashSHA256();
    }
}
