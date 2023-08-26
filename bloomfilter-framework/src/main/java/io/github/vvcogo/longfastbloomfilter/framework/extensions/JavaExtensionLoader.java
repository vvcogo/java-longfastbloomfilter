package io.github.vvcogo.longfastbloomfilter.framework.extensions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JavaExtensionLoader implements ExtensionLoader {

    private final Map<String, BloomFilterExtension> loadedExtensions = new HashMap<>();

    @Override
    public BloomFilterExtension loadExtension(File file) throws ExtensionLoadException {
        ExtensionProperties properties = loadExtensionProperties(file);
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
            Class<?> mainClazz = Class.forName(properties.getMainClassName(), true, classLoader);
            JavaBloomFilterExtension instance = (JavaBloomFilterExtension) mainClazz.getConstructor().newInstance();
            instance.load(classLoader, properties);
            loadedExtensions.put(properties.getName(), instance);
            return instance;
        } catch (MalformedURLException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            throw new ExtensionLoadException("Failed to load extension class", e);
        }
    }

    @Override
    public Collection<BloomFilterExtension> getLoadedExtensions() {
        return Collections.unmodifiableCollection(loadedExtensions.values());
    }

    @Override
    public BloomFilterExtension getExtension(String name) {
        return loadedExtensions.get(name);
    }

    private ExtensionProperties loadExtensionProperties(File file) throws ExtensionLoadException {
        Properties properties = new Properties();
        try (JarFile jar = new JarFile(file)) {
            ZipEntry propertiesEntry = jar.getEntry("extension.properties");
            try (InputStream inputStream = jar.getInputStream(propertiesEntry)) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new ExtensionLoadException("Extension file does not exists!", e);
        }
        return new ExtensionProperties(properties);
    }
}
