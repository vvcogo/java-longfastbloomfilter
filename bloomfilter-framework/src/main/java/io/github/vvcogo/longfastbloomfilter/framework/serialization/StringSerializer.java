package io.github.vvcogo.longfastbloomfilter.framework.serialization;

public class StringSerializer implements Serializer<String> {

    @Override
    public byte[] serialize(String string) {
        return string.getBytes();
    }
}
