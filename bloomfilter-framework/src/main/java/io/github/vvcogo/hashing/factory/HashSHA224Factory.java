package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashSHA224;
import io.github.vvcogo.hashing.algorithms.HashingAlgorithm;

public class HashSHA224Factory implements HashingFactory{
    @Override
    public HashingAlgorithm create() {
        return new HashSHA224();
    }
}
