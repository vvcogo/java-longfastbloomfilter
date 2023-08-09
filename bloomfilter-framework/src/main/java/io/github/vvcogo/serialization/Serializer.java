package io.github.vvcogo.serialization;

public interface Serializer<T> {

    byte[] serialize(T element);
}
