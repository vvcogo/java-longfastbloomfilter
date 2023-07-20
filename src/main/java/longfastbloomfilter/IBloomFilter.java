package longfastbloomfilter;

import java.util.Collection;

public interface IBloomFilter<T> {

    void add(T element);

    void addAll(Collection<T> elements);

    boolean mightContains(T element);

    void clear();

    BloomFilterConfiguration getConfiguration();
}
