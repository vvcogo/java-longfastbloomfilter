package io.github.vvcogo;

import java.io.Serializable;
import java.util.Collection;

public interface BloomFilter<T> extends Serializable, Cloneable {

    void add(T element);

    void addAll(Collection<T> elements);

    boolean mightContains(T element);

    boolean[] mightContains(Collection<T> elements);

    void clear();

    boolean isEmpty();

    BloomFilterConfiguration getConfiguration();

    boolean isCompatible(BloomFilter<T> other);

    void union(BloomFilter<T> other);

    void intersect(BloomFilter<T> other);
}
