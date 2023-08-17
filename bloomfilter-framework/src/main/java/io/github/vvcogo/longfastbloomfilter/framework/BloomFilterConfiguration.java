package io.github.vvcogo.longfastbloomfilter.framework;

import io.github.vvcogo.longfastbloomfilter.framework.bloomfilter.BloomFilter;
import io.github.vvcogo.longfastbloomfilter.framework.hashing.HashingAlgorithm;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;

import java.io.Serializable;

public record BloomFilterConfiguration<T>(long bitSetSize,
                                          long expectedNumberOfElements,
                                          int numberOfHashFunctions,
                                          double falsePositiveRate,
                                          HashingAlgorithm hashFunction,
                                          String bloomFilterType,
                                          Serializer<? super T> serializer) implements Serializable {
}
