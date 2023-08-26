package io.github.vvcogo.longfastbloomfilter.guavaextension;

import io.github.vvcogo.longfastbloomfilter.framework.extensions.JavaBloomFilterExtension;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterFactoryManager;

public class GuavaExtension extends JavaBloomFilterExtension {

    @Override
    public void onInit() {
        BloomFilterFactoryManager.registerFactory("guava", new GuavaBloomFilterFactory());
    }
}
