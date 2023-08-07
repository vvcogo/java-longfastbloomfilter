package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.HashingAlgorithm;
import io.github.vvcogo.hashing.algorithms.HashSHA1;

public class HashSHA512Factory implements HashingFactory{
    @Override
    public HashingAlgorithm create() {
        return new HashSHA1.HashSHA512();
    }
}
