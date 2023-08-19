package io.github.vvcogo.longfastbloomfilter.framework.extensions;

import java.io.File;

public interface ExtensionLoader {

    BloomFilterExtension loadExtension(File file);

}
