package io.github.vvcogo.longfastbloomfilter.framework.extensions;

import java.io.File;
import java.util.Collection;

public interface ExtensionLoader {

    BloomFilterExtension loadExtension(File file) throws ExtensionLoadException;

    Collection<BloomFilterExtension> getLoadedExtensions();

    BloomFilterExtension getExtension(String name);

}
