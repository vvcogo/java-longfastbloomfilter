package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashMD2;
import io.github.vvcogo.hashing.HashingAlgorithm;

public class HashMD2Factory implements HashingFactory{

    @Override
    public HashingAlgorithm create() {
        return new HashMD2();
    }
}
