package io.github.vvcogo;

import io.github.vvcogo.hashing.HashingAlgorithm;
import io.github.vvcogo.serialization.Serializer;

public record BloomFilterConfiguration<T>(long bitSetSize, long expectedNumberOfElements,
                                       int numberOfHashFunctions, double falsePositiveRate,
                                       HashingAlgorithm hashFunction, Serializer<T> serializer) {
}
