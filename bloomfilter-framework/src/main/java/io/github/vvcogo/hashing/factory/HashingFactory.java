package io.github.vvcogo.hashing.factory;

import io.github.vvcogo.hashing.algorithms.HashingAlgorithm;

public interface HashingFactory {

    HashingAlgorithm create();
}
