package io.github.vvcogo.longfastbloomfilter.framework.extensions;

import java.net.URLClassLoader;

public abstract class JavaBloomFilterExtension implements BloomFilterExtension {

    private URLClassLoader classLoader;
    private ExtensionProperties properties;

    void load(URLClassLoader classLoader, ExtensionProperties properties) {
        this.classLoader = classLoader;
        this.properties = properties;
    }

    @Override
    public ExtensionProperties getProperties() {
        return this.properties;
    }
}
