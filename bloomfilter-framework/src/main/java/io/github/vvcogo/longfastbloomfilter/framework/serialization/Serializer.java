package io.github.vvcogo.longfastbloomfilter.framework.serialization;

import java.io.Serializable;

public interface Serializer<T> extends Serializable {

    byte[] serialize(T element);
}
