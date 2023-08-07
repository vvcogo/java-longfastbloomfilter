package io.github.vvcogo.bitset;

import java.io.Serializable;

public interface BitSet extends Serializable, Cloneable {

    void set(long index);

    boolean get(long index);

    void clear();

    boolean isEmpty();

    void union(BitSet other);

    void intersect(BitSet other);

    boolean isCompatible(BitSet other);

    long getSize();

    BitSet clone();
}
