package io.github.vvcogo.longfastbloomfilter.framework.factory;

import java.util.HashMap;
import java.util.Map;

public final class BloomFilterFactoryManager {

    private static final Map<String, BloomFilterFactory> FACTORIES = new HashMap<>();

    static {
        registerFactory("longfastbloomfilter", new StandardLongBloomFilterFactory());
    }

    private BloomFilterFactoryManager() {
        throw new UnsupportedOperationException("Cannot create instance of " + getClass().getName());
    }

    public static void registerFactory(String id, BloomFilterFactory factory) {
        if (FACTORIES.containsKey(id)) {
            // FIXME: exception
            throw new RuntimeException("Factory already exists.");
        }
        FACTORIES.put(id, factory);
    }

    public static BloomFilterFactory getFactory(String id) {
        return FACTORIES.get(id);
    }
}
