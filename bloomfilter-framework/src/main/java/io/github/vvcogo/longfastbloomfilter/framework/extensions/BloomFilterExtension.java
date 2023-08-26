package io.github.vvcogo.longfastbloomfilter.framework.extensions;

public interface BloomFilterExtension {

    void onInit();

    ExtensionProperties getProperties();

}
