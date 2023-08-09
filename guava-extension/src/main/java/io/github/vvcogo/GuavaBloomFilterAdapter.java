package io.github.vvcogo;

public class GuavaBloomFilterAdapter<T> implements BloomFilter<T> {

    public GuavaBloomFilterAdapter(BloomFilterConfiguration<? super T> config){

    }


    @Override
    public void add(T element) {

    }

    @Override
    public boolean mightContains(T element) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public BloomFilterConfiguration<? super T> getConfiguration() {
        return null;
    }
}
