package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashingAlgorithm;
import io.github.vvcogo.hashing.algorithms.HashMurmur3KirschMitzenmacher;

public class HashMurmur3KirschMitzenmacherFactory implements HashingFactory{

    @Override
    public HashingAlgorithm create() {
        return new HashMurmur3KirschMitzenmacher();
    }
}
