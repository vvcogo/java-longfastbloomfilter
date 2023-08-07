package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashSHA512;
import io.github.vvcogo.hashing.HashingAlgorithm;

public class HashSHA512Factory implements HashingFactory{
    @Override
    public HashingAlgorithm create() {
        return new HashSHA512();
    }
}
