package io.github.vvcogo;

public interface Serializer<T> {

    byte[] serialize(T element);
}
