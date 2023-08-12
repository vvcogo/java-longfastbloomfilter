package io.github.vvcogo.longfastbloomfilter.framework.bitset;

import java.io.Serializable;

public interface BitSet extends Serializable {

    void set(long index);

    boolean get(long index);

    void clear();

    boolean isEmpty();

    long getSize();

    BitSet copy();
}
