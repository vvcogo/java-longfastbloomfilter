package io.github.vvcogo.longfastbloomfilter.framework.extensions;

import java.net.URLClassLoader;

public record ExtensionData (BloomFilterExtension extension, ExtensionProperties properties, URLClassLoader classLoader){
}
