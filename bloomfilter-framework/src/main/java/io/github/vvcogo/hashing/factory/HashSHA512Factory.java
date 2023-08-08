package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashingAlgorithm;
import io.github.vvcogo.hashing.algorithms.HashSHA512;

public class HashSHA512Factory implements HashingFactory{
    @Override
    public HashingAlgorithm create() {
        return new HashSHA512();
    }
}
