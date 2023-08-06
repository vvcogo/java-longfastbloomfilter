package io.github.vvcogo;

import java.util.Collection;

public interface BloomFilter<T> {

    void add(T elem);

    void addAll(Collection<T> collection);

    boolean mightContains(T elem);

    boolean[] mightContains(Collection<T> elems);

    void clear();
}
