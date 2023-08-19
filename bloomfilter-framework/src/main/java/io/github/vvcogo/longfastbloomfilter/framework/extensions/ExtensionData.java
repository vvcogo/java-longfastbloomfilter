package io.github.vvcogo.longfastbloomfilter.framework.extensions;

import java.net.URLClassLoader;

public class ExtensionData {

    private final BloomFilterExtension extension;
    private final ExtensionProperties properties;
    private final URLClassLoader classLoader;

    public ExtensionData(BloomFilterExtension extension, ExtensionProperties properties, URLClassLoader classLoader) {
        this.extension = extension;
        this.properties = properties;
        this.classLoader = classLoader;
    }

    public BloomFilterExtension getExtension() {
        return extension;
    }

    public ExtensionProperties getProperties() {
        return properties;
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }
}
