package io.github.vvcogo.longfastbloomfilter.framework.configuration.properties;

import io.github.vvcogo.longfastbloomfilter.framework.sus.InvalidConfigurationException;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.ObjectSerializer;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.Serializer;
import io.github.vvcogo.longfastbloomfilter.framework.serialization.SerializerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class SerializerProperty<T> implements OptionalProperty {

    public Serializer<? super T> checkProperty(Properties properties) {
        String serializerClass = properties.getProperty("serializer", ObjectSerializer.class.toString());
        try {
            return SerializerFactory.createSerializer(serializerClass);
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new InvalidConfigurationException("Failed to create instance of specified serializer", e);
        }
    }
}
