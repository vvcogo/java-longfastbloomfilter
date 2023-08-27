package io.github.vvcogo.longfastbloomfilter.framework.sus2;

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
