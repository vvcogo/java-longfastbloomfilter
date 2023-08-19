package io.github.vvcogo.longfastbloomfilter.guavaextension;

import io.github.vvcogo.longfastbloomfilter.framework.extensions.BloomFilterExtension;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterFactoryManager;

public class GuavaExtension implements BloomFilterExtension {

    @Override
    public void onInit() {
        BloomFilterFactoryManager.registerFactory("guava", new GuavaBloomFilterFactory());
    }
}
