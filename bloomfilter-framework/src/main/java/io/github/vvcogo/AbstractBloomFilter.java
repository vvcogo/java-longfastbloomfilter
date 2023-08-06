package io.github.vvcogo;

import java.util.Collection;

public abstract class AbstractBloomFilter implements BloomFilter<Long> {

    @Override
    public void add(Long elem) {
    }

    @Override
    public void addAll(Collection<Long> collection) {
    }

    @Override
    public boolean mightContains(Long elem) {
        return false;
    }

    @Override
    public boolean[] mightContains(Collection<Long> elms) {
        return new boolean[0];
    }

    @Override
    public void clear() {
    }
}
