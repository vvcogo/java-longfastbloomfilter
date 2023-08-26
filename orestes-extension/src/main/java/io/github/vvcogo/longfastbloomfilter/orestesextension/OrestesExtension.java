package io.github.vvcogo.longfastbloomfilter.orestesextension;

import io.github.vvcogo.longfastbloomfilter.framework.extensions.JavaBloomFilterExtension;
import io.github.vvcogo.longfastbloomfilter.framework.factory.BloomFilterFactoryManager;

public class OrestesExtension extends JavaBloomFilterExtension {

    @Override
    public void onInit() {
        BloomFilterFactoryManager.registerFactory("orestes", new OrestesBloomFilterFactory());
    }
}
