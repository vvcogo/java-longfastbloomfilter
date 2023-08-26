package io.github.vvcogo.longfastbloomfilter.framework.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class JavaSerializableSerializer implements Serializer<Serializable> {

    @Override
    public byte[] serialize(Serializable element) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(element);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("The specified object is not instanceof Serializable", e);
        }
    }
}
