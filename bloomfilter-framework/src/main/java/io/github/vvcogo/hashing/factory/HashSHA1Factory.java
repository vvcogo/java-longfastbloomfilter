package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashSHA1;
import io.github.vvcogo.hashing.HashingAlgorithm;

public class HashSHA1Factory implements HashingFactory{
    @Override
    public HashingAlgorithm create() {
        return new HashSHA1();
    }
}
