package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashSHA384;
import io.github.vvcogo.hashing.algorithms.HashingAlgorithm;

public class HashSHA384Factory implements HashingFactory{
    @Override
    public HashingAlgorithm create() {
        return new HashSHA384();
    }
}
