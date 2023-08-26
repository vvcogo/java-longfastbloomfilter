package io.github.vvcogo.longfastbloomfilter.framework.bloomfilter;

import io.github.vvcogo.longfastbloomfilter.framework.sus.BloomFilterConfiguration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface BloomFilter<T> extends Serializable {

    void add(T element);

    default void addAll(Iterable<T> elements) {
        for (T element : elements) {
            add(element);
        }
    }

    boolean mightContains(T element);

    default List<Boolean> mightContains(Iterable<T> elements) {
        List<Boolean> result = new ArrayList<>();
        for (T element : elements) {
            result.add(mightContains(element));
        }
        return result;
    }

    void clear();

    boolean isEmpty();

    BloomFilterConfiguration<? super T> getConfiguration();

    BloomFilter<T> copy();
}
