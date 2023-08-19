package io.github.vvcogo.longfastbloomfilter.framework.extensions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JavaExtensionLoader implements ExtensionLoader {

    private final Map<String, ExtensionData> loadedExtensions = new HashMap<>();

    @Override
    public BloomFilterExtension loadExtension(File file) {
        ExtensionProperties properties = loadExtensionProperties(file);
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
            Class<?> mainClazz = Class.forName(properties.getMainClassName(), true, classLoader);
            BloomFilterExtension instance = (BloomFilterExtension) mainClazz.getConstructor().newInstance();
            ExtensionData data = new ExtensionData(instance, properties, classLoader);
            this.loadedExtensions.put(properties.getName(), data);
            return instance;
        } catch (MalformedURLException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            // FIXME exception
            throw new RuntimeException(e);
        }
    }

    private ExtensionProperties loadExtensionProperties(File file) {
        Properties properties = new Properties();
        try (JarFile jar = new JarFile(file)) {
            ZipEntry propertiesEntry = jar.getEntry("extension.properties");
            try (InputStream inputStream = jar.getInputStream(propertiesEntry)) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            // FIXME exception
            throw new RuntimeException(e);
        }
        return new ExtensionProperties(properties);
    }

    public Map<String, ExtensionData> getLoadedExtensionsData() {
        return this.loadedExtensions;
    }

    public Set<String> getLoadedExtensions() {
        return this.loadedExtensions.keySet();
    }
}
