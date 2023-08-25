package io.github.vvcogo.longfastbloomfilter.framework.serialization;

public class ObjectSerializer implements Serializer<Object> {

    private final IntegerSerializer integerSerializer = new IntegerSerializer();

    @Override
    public byte[] serialize(Object element) {
        return integerSerializer.serialize(element.hashCode());
    }
}
