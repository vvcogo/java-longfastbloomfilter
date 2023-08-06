package io.github.vvcogo;

import java.io.Serializable;

public interface BitSet<T extends Number> extends Serializable {

    void set(T index);

    boolean get(T index);

    void clear();

}
