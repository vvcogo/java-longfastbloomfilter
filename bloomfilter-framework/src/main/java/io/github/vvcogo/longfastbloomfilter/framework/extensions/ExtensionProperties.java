package io.github.vvcogo.longfastbloomfilter.framework.extensions;

import java.util.Properties;

public class ExtensionProperties {

    private final String mainClassName;
    private final String name;
    private final String version;

    public ExtensionProperties(Properties properties) {
        this.mainClassName = properties.getProperty("main-class");
        this.name = properties.getProperty("name");
        this.version = properties.getProperty("version");
    }

    public String getMainClassName() {
        return this.mainClassName;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }
}
