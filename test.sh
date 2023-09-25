#!/bin/sh
java -jar ./out/artifacts/bloomfilter_test_jar/bloomfilter-test.jar input.txt query.txt config.properties
visualvm --start-memory-sampler $!
