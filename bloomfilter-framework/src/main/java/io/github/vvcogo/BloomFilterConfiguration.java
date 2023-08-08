package io.github.vvcogo;

import io.github.vvcogo.hashing.algorithms.HashingAlgorithm;

public record BloomFilterConfiguration(long bitSetSize, long expectedNumberOfElements,
                                       int numberOfHashFunctions, double falseProbabilityRate,
                                       HashingAlgorithm hashFunction) {
}
