package longfastbloomfilter;

import java.util.Collection;

public class StandardBloomFilter<T> implements IBloomFilter<T> {

    private final BloomFilterConfiguration configuration;

    public StandardBloomFilter(BloomFilterConfiguration configuration) {this.configuration = configuration;}

    @Override
    public void add(T element) {

    }

    @Override
    public void addAll(Collection<T> elements) {
        for (T element : elements) {
            add(element);
        }
    }

    @Override
    public boolean mightContains(T element) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public BloomFilterConfiguration getConfiguration() {
        return this.configuration;
    }
}
