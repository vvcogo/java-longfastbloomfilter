package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashMD5;
import io.github.vvcogo.hashing.HashingAlgorithm;

public class HashMD5Factory implements HashingFactory{
    @Override
    public HashingAlgorithm create() {
        return new HashMD5();
    }
}
