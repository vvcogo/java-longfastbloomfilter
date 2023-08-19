package io.github.vvcogo.longfastbloomfilter.orestesextension;

import io.github.vvcogo.longfastbloomfilter.framework.extensions.BloomFilterExtension;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterFactoryManager;

public class OrestesExtension implements BloomFilterExtension {

    @Override
    public void onInit() {
        BloomFilterFactoryManager.registerFactory("orestes", new OrestesBloomFilterFactory());
    }
}
