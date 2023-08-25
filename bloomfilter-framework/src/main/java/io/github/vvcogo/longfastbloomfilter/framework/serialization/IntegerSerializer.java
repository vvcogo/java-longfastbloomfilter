package io.github.vvcogo.longfastbloomfilter.framework.serialization;

import java.nio.ByteBuffer;

public class IntegerSerializer implements Serializer<Integer> {

    @Override
    public byte[] serialize(Integer element) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(element);
        return buffer.array();
    }
}
