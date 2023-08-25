package io.github.vvcogo.longfastbloomfilter.framework.serialization;

import java.lang.reflect.InvocationTargetException;

public final class SerializerFactory {

    private SerializerFactory() {
        throw new UnsupportedOperationException("Cannot create instance of " + getClass().getName());
    }

    @SuppressWarnings("unchecked")
    public static <T> Serializer<T> createSerializer(String className) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        return (Serializer<T>) instance;
    }

}
