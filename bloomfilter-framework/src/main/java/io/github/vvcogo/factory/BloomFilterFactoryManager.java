package io.github.vvcogo.factory;

import java.util.HashMap;
import java.util.Map;

public final class BloomFilterFactoryManager {

    private static final Map<String, BloomFilterFactory> FACTORIES = new HashMap<>();

    private BloomFilterFactoryManager() { }

    public static void registerFactory(BloomFilterFactory factory) {
        String id = factory.getFactoryId();
        if (FACTORIES.containsKey(id)) {
            throw new RuntimeException("Factory already exists.");
        }
        FACTORIES.put(id, factory);
    }

    public static BloomFilterFactory getFactoryFromId(String id) {
        return FACTORIES.get(id);
    }
}
